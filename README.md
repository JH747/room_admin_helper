# room_admin_helper

## set environment

### download and install chrome

'''
wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
sudo sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
sudo apt update
sudo apt install google-chrome-stable
'''

### Xvfb

'''
sudo apt-get install xvfb
Xvfb :99 -screen 0 1920x1080x24 &
export DISPLAY=:99
'''

### dbus

'''
sudo apt-get install -y dbus
sudo service dbus start
'''

- Ensure that the DBUS_SESSION_BUS_ADDRESS environment variable is correctly set. In some cases, this variable may not be set properly in WSL.
- You can set it manually with:
  '''
  export $(dbus-launch)
  '''
- This command starts a new D-Bus session and exports the DBUS_SESSION_BUS_ADDRESS to the environment.

### upower

'''
sudo apt install upower
'''

**_check if chrome runs properly on system before starting server with:_**
'''
google-chrome --no-sandbox --disable-gpu
'''
