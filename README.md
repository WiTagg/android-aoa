# README #

1、The activity module is interface related code, and DeviceDetailActivity and 
    DeviceSettingActivity are mqtt related pages The DeviceDetailActivity is the 
    location device details page; DeviceSettingActivity is the setting page when 
    locating, including kalman Setting of intensity and number of display position 
    points.

2、The adpter module is the page and data processing related page

3、The customerview module is a user-defined view related module

4、Fragment module is the page related to switching on the bottom tabbar, and 
    Bluetooth Fragment is the Bluetooth broadcast page , MqttFragment is the mqtt 
    server configuration and connection page.

5、The manager is the core module, and the Bluetooth Manager is a Bluetooth broadcast
    management class, which has the following functions: 1 Bluetooth broadcast package 
    initialization 2 Bluetooth broadcast package Sending and callback; MqttClient is
    the mqtt client class

6、Tools is a tool module, BleUtil is a tool function, and SharePreferenceUtil is used
    to save data to local classes.
