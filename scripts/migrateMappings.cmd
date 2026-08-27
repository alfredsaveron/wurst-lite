@echo off
echo                               /^^\
echo            L L               /   \               L L
echo         __/^|/^|_             /  .  \             _^|\^|\__
echo        /_^| [_[_\           /     .-\           /_]_] ^|_\
echo       /__\  __`-\_____    /    .    \    _____/-`__  /__\
echo      /___] /=@^>  _   {^>  /-.         \  ^<}   _  ^<@=\ [___\
echo     /____/     /` `--/  /      .      \  \--` `\     \____\
echo    /____/  \____/`-._^> /               \ ^<_.-`\____/  \____\
echo   /____/    /__/      /-._     .   _.-  \      \__\    \____\
echo  /____/    /__/      /         .         \      \__\    \____\
echo ^|____/_  _/__/      /          .          \      \__\_  _\____^|
echo  \__/_ ``_^|_/      /      -._  .        _.-\      \_^|_`` _\___/
echo    /__`-`__\      ^<_         `-; Wurst Lite_^>      /__`-`__\
echo       `-`           `-._       ;       _.-`           `-`
echo                         `-._   ;   _.-`
echo                             `-._.-`
echo.
echo OLD VERSION!!! Only use for mappings that don't support v2.
cd ..
set /p targetMappings=Target Mappings (net.fabricmc.yarn): 
call gradlew.bat migrateMappings -PtargetMappingsArtifact="%targetMappings%" -PinputDir="%cd%/src/main/java" -PoutputDir="%cd%/src/main/new java"
rmdir /s /q "%cd%/src/main/java"
move "%cd%/src/main/new java" "%cd%/src/main/java"
rem gradlew.bat downloadAssets
pause