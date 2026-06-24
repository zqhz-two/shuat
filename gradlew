#!/bin/sh

APP_HOME=$( cd "${APP_HOME:-./}" && pwd -P ) || exit

APP_NAME="Gradle"
APP_BASE_NAME=${0##*/}

exec "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
