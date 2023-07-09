# Dynamic-File-Storage
## Description
This project was intended to support peer-to-peer dynamic encrypted file storage (supporting only one file currently) through a LAN connection.

## Setup

First, update the AES-256 (AES_KEY) key for the client and server to any random string.

Second, set the file location to the location of some document you wish to transfer between computers (FILE_LOCATION) on both the server and client.

Third, set the logs folder location for the server (LOGS_FOLDER).

Fourth, find the server's [LAN IP](https://www.whatismybrowser.com/detect/what-is-my-local-ip-address) and update the server address (HOST_ADDRESS).

Fifth, add an outbound/inbound rule on the Windows Defender Firewall for the port (HOST_PORT) on the server machine.

Finally, tune any of the settings for port, lines to read from the command prompt, and the timeout length.
