var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var date_utils = require('date-utils');

var SerialPort = require('serialport'),
    portName = 'COM5',//포트 번호 확인 후 추가
    port = new SerialPort(portName),
    SerialPort = 0;

port.on('open', function () {
    console.log('Serial Port OPEN');
});

io.on('connection', function (socket) {
    console.log('user connection.');

    socket.on('LightOn', function (data) {
        console.log('LightOn');
        port.write('3');
    })
    socket.on('LightOff', function (data) {
        console.log('LightOff');
        port.write('4');
    })
    socket.on('AirOn', function (data) {
        console.log('AirOn');
        port.write('5');
    })
    socket.on('AirOff', function (data) {
        console.log('AirOff');
        port.write('6');
    })
    socket.on('BoilerOn', function (data) {
        console.log('BoilerOn');
        port.write('7');
    })
    socket.on('BoilerOff', function (data) {
        console.log('BoilerOff');
        port.write('8');
    })

    socket.on('disconnect', function () {
        console.log('disconnection');
    })
});

app.get('/', function (req, res) {
    console.log("get success");
});

http.listen(3000, function () {
    console.log('3000 port listening');
});
