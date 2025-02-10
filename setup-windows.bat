@echo off
setlocal EnableDelayedExpansion

:: Define SDK paths
set "SDK_DIR=%USERPROFILE%\Android\Sdk"
set "CMDLINE_TOOLS_DIR=%SDK_DIR%\cmdline-tools"
set "CMDLINE_BIN=%CMDLINE_TOOLS_DIR%\tools\bin"
set "PLATFORM_TOOLS_DIR=%SDK_DIR%\platform-tools"
set "JAVA_DIR=C:\Program Files\Java\jdk-17"

:: Check if Android SDK is installed
if exist "%CMDLINE_BIN%" (
    echo Android SDK is already installed.
) else (
    echo Android SDK not found. Installing...
    mkdir "%SDK_DIR%"
    cd /d "%SDK_DIR%"

    echo Downloading Android SDK...
    curl -o cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip

    echo Extracting SDK files...
    powershell -command "Expand-Archive -Path cmdline-tools.zip -DestinationPath cmdline-tools"
    move cmdline-tools\cmdline-tools cmdline-tools\tools
    del cmdline-tools.zip

    echo Accepting licenses...
    call "%CMDLINE_BIN%\sdkmanager.bat" --licenses
    
    echo Installing required SDK components...
    call "%CMDLINE_BIN%\sdkmanager.bat" --install "platforms;android-35" "build-tools;34.0.0" "platform-tools"

    setx ANDROID_HOME "%SDK_DIR%"
    setx PATH "%SDK_DIR%\cmdline-tools\tools\bin;%SDK_DIR%\platform-tools;%PATH%"

    echo Android SDK installation completed successfully!
)

echo Setup completed. Please restart your terminal to apply changes!
endlocal
