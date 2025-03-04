# 빌드 스테이지
FROM gradle:8-jdk21 AS build

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 캐시를 활용하여 종속성 다운로드
COPY build.gradle settings.gradle ./
RUN gradle build -x test --no-daemon || return 0

# 프로젝트 파일 복사
COPY . .

# Gradle을 사용하여 프로젝트 빌드
RUN gradle clean build -x test --no-daemon

# 런타임 스테이지
FROM openjdk:21-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 설정
EXPOSE 8080

# 컨테이너 실행 시 실행할 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]