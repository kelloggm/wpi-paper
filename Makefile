all: wpi.pdf

wpi.pdf: bib-update
	latexmk -pdf -interaction=nonstopmode -f wpi.tex

wpi-notodos.pdf: wpi.pdf
	pdflatex "\def\notodocomments{}\input{wpi}"
	pdflatex "\def\notodocomments{}\input{wpi}"
	cp -pf wpi.pdf $@

# Upload onefile.zip to the publisher website
onefile.zip: onefile.tex
	zip onefile.zip onefile.tex acmart.cls ACM-Reference-Format.bst
onefile.tex: $(filter-out onefile.tex, $(wildcard *.tex))
	latex-process-inputs wpi.tex > onefile.tex


# This target creates:
#   https://homes.cs.washington.edu/~mernst/tmp678/wpi.pdf
web: wpi-notodos.pdf
	cp -pf $^ ${HOME}/public_html/tmp678/wpi.pdf
.PHONY: wpi-singlecolumn.pdf wpi-notodos.pdf

martin: wpi.pdf
	open $<

export BIBINPUTS ?= .:bib
bib:
ifdef PLUMEBIB
	ln -s ${PLUMEBIB} $@
else
	git clone https://github.com/mernst/plume-bib.git $@
endif
.PHONY: bib-update
bib-update: bib
# Even if this command fails, it does not terminate the make job.
# However, to skip it, invoke make as:  make NOGIT=1 ...
ifndef NOGIT
	-(cd bib && make)
endif

TAGS: tags
tags:
	etags `latex-process-inputs -list wpi.tex`

## TODO: this should not delete ICSE2020-submission.pdf
clean:
	rm -f *.bbl *.aux *~ wpi.pdf *.blg *.log TAGS
