## build stage

# 베이스 이미지 설정
FROM python:3.10-slim

# 작업 디렉토리 생성
WORKDIR /app

# 필수 시스템 패키지 설치
#C/C++ 컴파일 및 빌드를 위해 필요한 필수 도구 모음
#외부 C 라이브러리의 컴파일 및 링크 플래그를 관리, 설치된 MySQL/MariaDB 클라이언트 라이브러리의 경로와 플래그를 찾는 데 사용
#MySQL 클라이언트 라이브러리와 개발 헤더 파일을 제공. Python의 mysqlclient와 같은 패키지가 MySQL 서버와 통신할 수 있도록 함
RUN apt update && apt install -y \
    build-essential \
    pkg-config \
    libmariadb-dev \
    libmariadb-dev-compat \
    && apt clean


# Python 패키지 설치를 위해 필요한 파일 복사
COPY ./backend/requirements.txt .
#COPY . .

# Python 패키지 설치
RUN pip install --no-cache-dir --upgrade pip \
    && pip install --no-cache-dir -r requirements.txt

## production stage

# 프로젝트 코드 복사
COPY ./backend .

# 정적 파일 수집
RUN python manage.py collectstatic --noinput

# 환경 변수 설정
#ENV PYTHONUNBUFFERED 1
#ENV DJANGO_SETTINGS_MODULE config.settings

# 포트 노출
EXPOSE 8000

# 명령 실행
#ENTRYPOINT ["python", "manage.py", "runserver", "0.0.0.0:8000"]
