#!/usr/bin/env bash



# ./src/main/resources/scripts/one.sh -this -is -a -test

function parse_cli() {
    echo $@
    echo  Total number of arguments $#
    testvar=
    echo $1
    echo starting loop

    #for ((i = 1; i <= $#; i++ )); do
        #echo -- ${!i}
    #done

    for ((i = 1; i <= $#; i++)); do
        echo ----- $i  ${!i}
        case "${!i}" in
          -is|--is)
            #i=$((i + 1)) IMPORTANT: we are not incrementing i because we are not passing any value for argument e.g. -this thisvalue -is isvalue ....
            echo found is
          ;;
          -test|--test)
            #i=$((i + 1))
            echo found test
          ;;
          -this|--this)
            #i=$((i + 1))
            echo found this
          ;;
          -a|--a)
            #i=$((i + 1))
            echo found a
          ;;
          *)
        esac
    done
}

parse_cli "$@"
