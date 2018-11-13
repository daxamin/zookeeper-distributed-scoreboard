.PHONY: do_script

do_script:
	sh setup.sh

prerequisites: do_script

target: prerequisites