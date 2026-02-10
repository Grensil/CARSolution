# CARSolution

Android 개발에서 사용되는 다양한 기술과 아키텍처 패턴을 직접 적용하고 테스트해보기 위한 토이 프로젝트입니다.

실제 서비스 구현보다는 **멀티모듈 클린 아키텍처, Jetpack Compose, 코드 품질 자동화** 등 현업에서 쓰이는 기술 스택을 하나의 프로젝트에서 종합적으로 실험하는 것이 목표입니다.

## 테스트한 기술 목록

### 멀티모듈 클린 아키텍처

11개 모듈로 분리된 클린 아키텍처를 구성하고, 모듈 간 의존성 규칙을 자동으로 검증합니다.

```
:app
├── :core:common          # 순수 Kotlin 유틸 (UiState 등)
├── :core:navigation      # 공유 Navigation Graph
├── :core:designsystem    # Material3 테마 / 컴포넌트
├── :domain               # kotlin-jvm 모듈 (Android 의존성 없음)
├── :data                 # Repository 구현, Retrofit, Firebase
├── :feature:insurance    # 보험 탭
├── :feature:fuel         # 주유 탭
├── :feature:usedcar      # 중고차 탭
├── :feature:accident     # 사고 탭
└── :feature:auth         # 인증 (Splash, 차량번호, 본인인증)
```

**Module Graph Assert**로 아키텍처 규칙을 강제합니다:
- `feature` -> `data` 직접 접근 금지 (반드시 `domain`을 경유)
- `domain`은 어떤 모듈도 알지 못함 (순수 비즈니스 로직)
- `data` -> `feature` 역방향 의존 금지

### Jetpack Compose + Stability 최적화

- **Compose BOM 2025.12.01** 기반 전체 UI 구성
- **Compose Stability Analyzer** 플러그인으로 안정성 분석
- `compose-stability.conf`에 domain model을 등록하여 **전체 Composable 100% SKIP** 달성
- 순수 Kotlin JVM 모듈(`domain`)의 data class가 Compose 컴파일러에 불안정으로 추론되는 문제를 해결

### Navigation Compose 2.9.5 (Type-Safe Routes)

- `@Serializable` data class/object로 타입 안전한 라우팅
- **Nested Navigation Graph**: 각 탭이 독립적인 NavGraph를 가짐
- **탭 상태 보존**: `saveState = true` + `restoreState = true`로 탭 전환 시 상태 유지
- **공유 화면**: `vehicleDetailScreen()` 확장 함수로 여러 탭에서 같은 화면 재사용

### Hilt 의존성 주입

- `@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel` 전체 적용
- UseCase에 `@Inject constructor` 직접 주입 (별도 `@Provides` 불필요)
- `@Binds`를 사용한 Repository 인터페이스-구현체 바인딩

### Firebase Phone Auth (SMS 인증)

- Firebase Authentication 기반 휴대폰 본인인증 흐름 구현
- 인증 코드 발송 -> OTP 입력 -> 검증 전체 플로우
- 타이머 UI, 재발송 로직 포함

### Retrofit 네트워크 레이어

- Retrofit 2.11 + kotlinx-serialization converter
- OkHttp Logging Interceptor
- Hilt `@Module`을 통한 네트워크 객체 제공

### 코드 품질 자동화 파이프라인

로컬 개발부터 CI까지 코드 품질을 자동으로 관리하는 파이프라인을 구축했습니다.

| 도구 | 역할 | 적용 방식 |
|------|------|-----------|
| **Detekt** | Kotlin 정적 분석 (코드 스멜, 복잡도, 네이밍) | 전체 subproject 자동 적용 |
| **Kotlinter** | ktlint 기반 코드 포맷팅 | `formatKotlin` (자동 수정) / `lintKotlin` (검사) |
| **SonarLint** | SonarQube 없이 로컬 코드 품질 분석 | Kotlin 모듈 자동 적용 |
| **Dependency Guard** | 의존성 변경 감지 (baseline diff) | `releaseRuntimeClasspath` 모니터링 |
| **Module Graph Assert** | 모듈 의존성 규칙 위반 검사 | maxHeight 4, 역방향 의존 금지 규칙 |
| **Android Cache Fix** | Gradle 빌드 캐시 최적화 | Android 모듈 자동 적용 |
| **Compose Stability Analyzer** | Composable 안정성/스킵 가능 여부 분석 | app 모듈 적용 |

#### Git Pre-commit Hook

커밋 시 자동으로 코드 포맷팅 및 검증을 수행합니다:

```
commit 실행
  → formatKotlin (자동 수정)
  → 수정된 파일 re-stage
  → lintKotlin (검사)
  → 통과 시 커밋 완료
```

Gradle Sync 시 hook이 자동 설치됩니다.

#### GitHub Actions CI

`main` 브랜치에 push 또는 PR 생성 시 **Detekt**와 **Ktlint** 검사가 병렬로 실행됩니다.

### Detekt Compose 최적화 설정

Compose 프로젝트 특성에 맞게 Detekt 규칙을 커스터마이징했습니다:

- `FunctionNaming`: 대문자 시작 허용 (Composable 함수)
- `LongMethod`: 100줄 허용 (Compose UI 함수 특성)
- `LongParameterList`: 10개 허용, default parameter 무시
- `MagicNumber`: property, local variable, annotation 무시
- `CyclomaticComplexMethod`: when 표현식 무시, 허용 복잡도 20

## 기술 스택

| 카테고리 | 기술 |
|----------|------|
| Language | Kotlin 2.2.21 |
| UI | Jetpack Compose (BOM 2025.12.01), Material3 + Dynamic Color |
| Navigation | Navigation Compose 2.9.5 + kotlinx-serialization |
| DI | Hilt 2.58 + KSP |
| Network | Retrofit 2.11 + OkHttp 4.12 + kotlinx-serialization |
| Auth | Firebase Authentication (Phone Auth) |
| Build | AGP 8.12.3, Gradle Version Catalog |
| Code Quality | Detekt, Kotlinter, SonarLint, Dependency Guard, Module Graph Assert |
| CI | GitHub Actions |

## 빌드 및 실행

```bash
# 빌드
./gradlew assembleDebug

# 코드 품질 검사
./gradlew detektMain       # 정적 분석
./gradlew lintKotlin       # 코드 스타일 검사
./gradlew formatKotlin     # 자동 포맷팅

# 의존성 검사
./gradlew dependencyGuard          # baseline 비교
./gradlew dependencyGuardBaseline  # baseline 갱신

# 모듈 그래프 검증
./gradlew assertModuleGraph
```
