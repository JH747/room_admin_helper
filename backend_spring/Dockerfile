# 베이스 이미지 설정
FROM openjdk:17-jdk-alpine

# 애플리케이션 디렉토리 생성
WORKDIR /app

# 애플리케이션 JAR 파일 복사
COPY build/libs/backend_spring-0.0.1-SNAPSHOT.jar app.jar

# 포트 노출
EXPOSE 8080

# docker run 명령시 이행.
# ENTRYPOINT는 무조건이며, CMD의 경우 오버라이드될 수 있다.
#ENTRYPOINT ["java", "-jar"]
#CMD ["app.jar"]
