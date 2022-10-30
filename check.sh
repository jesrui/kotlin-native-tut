#!/bin/bash
set -euo pipefail

# excutable suffix is either .exe (on windows) or .kexe
case "$(uname)" in
	MINGW*)	SUFFIX=.exe;;
	*) 	SUFFIX=.kexe;;
esac

DEBUG_EXE=$(echo ./build/bin/native*/debugExecutable/kotlin-native-tut$SUFFIX)
RELEASE_EXE=$(echo ./build/bin/native*/releaseExecutable/kotlin-native-tut$SUFFIX)

check_usage() {
	local exe=$1
	local -r expected_usage='Value for option --input should be always provided in command line.
Usage: example options_list
Options: 
    --input, -i -> Input file (always required) { String }
    --output, -o -> Output file name { String }
    --format, -f [CSV] -> Format for output file { Value should be one of [html, csv, pdf] }
    --stringFormat, -sf [csv] -> Format as string for output file { Value should be one of [html, csv, pdf] }
    --debug, -d [false] -> Turn on debug mode 
    --eps [0.01] -> Observational error { Double }
    --help, -h -> Usage info 

'

	diff -u <($exe 2>&1) <(echo -n "$expected_usage")
}

check_output() {
	local exe=$1
	local -r expected_output='hello from the program
1
2
3
'
	rm -f out.txt
	$exe -i dontcare

	diff -u out.txt <(echo -n "$expected_output")
}

for exe in "$DEBUG_EXE" "$RELEASE_EXE"; do
	check_usage "$exe"
	check_output "$exe"
done
