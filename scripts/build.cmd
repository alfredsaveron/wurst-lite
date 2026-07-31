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
echo Formatting code with Spotless...
cd ..
call gradlew.bat spotlessApply
echo.
echo Building Wurst...
call gradlew.bat build
echo.
echo Done. Check for errors above before closing this window!
pause