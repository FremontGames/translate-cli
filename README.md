# TRANSLATE-CLI

Automatic translation tool.

## Features

- JSON (file to file)

## Getting started

run on windows
install Java

## Syntax

```
java -jar translate-cli-1.0.1.jar
       [--type-jsonfile]
       [--input=<FilePath>]
       [--input-lang=<LangLocale>]
       [--output=<FilePath>]
       [--output-lang=<Langlocale>]
```

## Command Parameters

```
java -jar translate-cli-1.0.1.jar [ parameters ]
```

| Parameters and argument specifications | Description      |
|----------------------------------------|------------------|
| `--type-jsonfile`                 | JSON File type    |
| `--input` *FilePath*                |                 |
| `--input-lang` *LangLocale*        |                  |
| `--output` *FilePath*               |                  |
| `--output-lang` *LangLocale*       |                   |

## Examples

launch command then autorize PhantomJS execution

Execute

```
java -jar translate-cli-1.0.0.jar --input=./input/test/resources/test-json_from_en_to_it-input.json --output=./test-json_from_en_to_it-output.json --input-lang=en --output-lang=it
```

Script for your project

```
# translate.bat

cd "c:\Users\damien\git\translate-cli"

set MYFILE="c:\Users\damien\git\project-2048\Project 2048\Assets\Project 2048\Resources\i18n\locale"
set MYVERSION=1.0.1

set MYLANG=fr
java -jar translate-cli-%MYVERSION%.jar --input=%MYFILE%-en.json --input-lang=en --output=%MYFILE%-%MYLANG%.json --output-lang=%MYLANG%
set MYLANG=ja
java -jar translate-cli-%MYVERSION%.jar --input=%MYFILE%-en.json --input-lang=en --output=%MYFILE%-%MYLANG%.json --output-lang=%MYLANG%
set MYLANG=ko
java -jar translate-cli-%MYVERSION%.jar --input=%MYFILE%-en.json --input-lang=en --output=%MYFILE%-%MYLANG%.json --output-lang=%MYLANG%
set MYLANG=zh-TW
java -jar translate-cli-%MYVERSION%.jar --input=%MYFILE%-en.json --input-lang=en --output=%MYFILE%-%MYLANG%.json --output-lang=%MYLANG%
set MYLANG=de
java -jar translate-cli-%MYVERSION%.jar --input=%MYFILE%-en.json --input-lang=en --output=%MYFILE%-%MYLANG%.json --output-lang=%MYLANG%
set MYLANG=pt
java -jar translate-cli-%MYVERSION%.jar --input=%MYFILE%-en.json --input-lang=en --output=%MYFILE%-%MYLANG%.json --output-lang=%MYLANG%
set MYLANG=es
java -jar translate-cli-%MYVERSION%.jar --input=%MYFILE%-en.json --input-lang=en --output=%MYFILE%-%MYLANG%.json --output-lang=%MYLANG%
set MYLANG=it
java -jar translate-cli-%MYVERSION%.jar --input=%MYFILE%-en.json --input-lang=en --output=%MYFILE%-%MYLANG%.json --output-lang=%MYLANG%
set MYLANG=ru
java -jar translate-cli-%MYVERSION%.jar --input=%MYFILE%-en.json --input-lang=en --output=%MYFILE%-%MYLANG%.json --output-lang=%MYLANG%
set MYLANG=hi
java -jar translate-cli-%MYVERSION%.jar --input=%MYFILE%-en.json --input-lang=en --output=%MYFILE%-%MYLANG%.json --output-lang=%MYLANG%
```

## Roadmap

- Basic (terminal)
- Text (file to file)
