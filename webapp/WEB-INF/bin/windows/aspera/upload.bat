set source=%1
set dest=%2
set user=%3
set password=%4
set host=%5
set ASPERA_SCP_PASS=%password%

@#-T to disable encryption

ascp -T %source%  %user%@%host%:%dest%
