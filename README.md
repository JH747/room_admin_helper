# room_admin_helper

여러 플랫폼에 걸친 객실 판매 수량 시각화, 오버부킹 방지, 소모품목 계산 등을 위한 프로젝트

## 1. 사용방법

### 1-1. 다운로드 및 의존성 설치

> ```
> git clone https://github.com/JH747/room_admin_helper.git
> cd frontend
> npm install
> cd ../backend_spring
> ./gradlew build
> cd ../backend
> pip install -r requirements.txt
> ```

### 1-2. 로컬 환경 설정

#### 1-2-1. MYSQL

- 포트: 3306
- username: dev
- password: 1234

#### 1-2-2. redis

- 포트: 6379
- username: user
- password: secret

### 1-3. 로컬 환경에서 실행

> ```
> cd ../frontend
> nohup bash -c "npm run dev" > nextjs.log 2>&1 &
> cd ../backend_spring
> nohup bash -c "java -jar /backend_spring/build/libs/backend_spring-0.0.1-SNAPSHOT.jar" > spring.log 2>&1 &
> cd ../backend
> nohup bash -c "python manage.py runserver" > django.log 2>&1 &
> ```

### 1-4. additional environment setting(Ubuntu server based)

#### download and install chrome

> ```
> wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
> sudo sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
> sudo apt update
> sudo apt install google-chrome-stable
> ```

**_check if chrome runs properly on system before starting server with:_**

```
google-chrome --no-sandbox --disable-gpu
```

**_you may have to restart system even after following steps below_**

#### Xvfb(X Virtual Framebuffer)

- 가상 프레임버퍼에서 작동하는 X Server의 일종입니다. 물리적 디스플레이나 입력 장치 없이 가상 메모리 상에서 그래픽 작업을 수행할 수 있도록 합니다.
- 실제 화면에 렌더링되지 않으며, 메모리의 가상 프레임버퍼에 그래픽 출력이 생성됩니다.
- X 응용 프로그램이 그래픽 환경을 필요로 할 때, 물리적 화면 없이도 실행할 수 있도록 가상 디스플레이를 제공합니다.
- if your system lacks Xvfb:
  > ```
  > sudo apt-get install xvfb
  > Xvfb :99 -screen 0 1920x1080x24 &
  > export DISPLAY=:99
  > ```

#### D-Bus

- if error relating your system lacks D-Bus service occurs:
  > ```
  > sudo apt-get install -y dbus
  > sudo service dbus start
  > ```
- Ensure that the DBUS_SESSION_BUS_ADDRESS environment variable is correctly set. In some cases, this variable may not be set properly in WSL.
- You can set it manually with:
  > ```
  > export $(dbus-launch)
  > ```
- This command starts a new D-Bus session and exports the DBUS_SESSION_BUS_ADDRESS to the environment.

#### UPower

- if error relating your system lacks UPower occurs:
  > ```
  > sudo apt install upower
  > ```

## 2. 차후 업데이트 사항

django 서버에 과거 객실별 예약 수량을 분석하여 이후 예약을 예측하는 모델 탑재

## 3. 제작자

Junhyung Oh
