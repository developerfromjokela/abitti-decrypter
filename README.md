# abitti-decrypter
Abitti's "custom" .meb format (just a zip, nothing special), files are encrypted (just AES, nothing special, c'mon what were you expecting from "datanomi" people?). My script decrypts them.

## How to use
1. Download the .jar file from releases.
2. Enter this into your command-line:

 `java -jar mebextractor.jar <password> <encrypted_file> <output>`
 
 ## What can/should I extract with this utility?
 In this website https://oma.abitti.fi/ you can create exams and download your own .meb files.
 
 .meb file is just "rebreanded" zip archive, which contains encrypted files.
 
 .bin files contained in the archive are encrypted, thus can be decrypted with this utility.
 
 ## .meb fileformat documentation
 1. .meb = .zip. They're same
 2. .bin files are encrypted with `AES/CTR/NoPadding` method
 3. Key calculation is done using `pbkdf2`. Learn more in the code.
