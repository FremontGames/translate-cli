# Automatic translation tool.

## Features

- JSON (file to file)

## Getting started

run on windows
install Java

## Usage

launch command then autorize PhantomJS execution

```
java -jar translator-cli-1.0.0.jar --src=./src/test/resources/test-json_from_en_to_it-input.json --dest=./test-json_from_en_to_it-output.json --src_lang=en --dest_lang=it
```

batching

```
set MYFILE="c:\Users\damien\git\project-2048\Project 2048\Assets\Project 2048\Resources\i18n\locale"

set MYLANG=ja
java -jar translator-cli-1.0.0.jar --src=%MYFILE%-en.json --src_lang=en --dest=%MYFILE%-%MYLANG%.json --dest_lang=%MYLANG%
set MYLANG=ko
java -jar translator-cli-1.0.0.jar --src=%MYFILE%-en.json --src_lang=en --dest=%MYFILE%-%MYLANG%.json --dest_lang=%MYLANG%
```

## Roadmap

- Basic (terminal)
- Text (file to file)
