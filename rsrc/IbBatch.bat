@echo off

rem Kommandozeilenparameter zum shell öffnen cmd /c start

echo         ********************************************************
echo         *                IB-Batch-File ausgefuehrt             *
echo         *                                                      *
echo         ********************************************************

PING 1.1.1.1 -n 1 -w 5000

@exit 0

