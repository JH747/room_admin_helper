# room_admin_helper

## set environment(Linux based)

### download and install chrome

> '''
> wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
> sudo sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
> sudo apt update
> sudo apt install google-chrome-stable
> '''

**_check if chrome runs properly on system before starting server with:_**
'''
google-chrome --no-sandbox --disable-gpu
'''

**_you may have to restart system even after following steps below_**

### Xvfb(X Virtual Framebuffer)

- 가상 프레임버퍼에서 작동하는 X Server의 일종입니다. 물리적 디스플레이나 입력 장치 없이 가상 메모리 상에서 그래픽 작업을 수행할 수 있도록 합니다.
- 실제 화면에 렌더링되지 않으며, 메모리의 가상 프레임버퍼에 그래픽 출력이 생성됩니다.
- X 응용 프로그램이 그래픽 환경을 필요로 할 때, 물리적 화면 없이도 실행할 수 있도록 가상 디스플레이를 제공합니다.
- if your system lacks Xvfb:
  > '''
  > sudo apt-get install xvfb
  > Xvfb :99 -screen 0 1920x1080x24 &
  > export DISPLAY=:99
  > '''

### D-Bus

- if error relating your system lacks D-Bus service occurs:
  > '''
  > sudo apt-get install -y dbus
  > sudo service dbus start
  > '''
- Ensure that the DBUS_SESSION_BUS_ADDRESS environment variable is correctly set. In some cases, this variable may not be set properly in WSL.
- You can set it manually with:
  > '''
  > export $(dbus-launch)
  > '''
- This command starts a new D-Bus session and exports the DBUS_SESSION_BUS_ADDRESS to the environment.

### UPower

- if error relating your system lacks UPower occurs:
  > '''
  > sudo apt install upower
  > '''
