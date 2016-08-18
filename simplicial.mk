JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

simplicial: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_simplicial_detection_result/$(i))

$(RESULTSDIR)/efficient_simplicial_detection_result/%:
	mkdir -p $(RESULTSDIR)/efficient_simplicial_detection_result
	$(JVM) efficientdetection/DetectSimplicialVertex $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_simplicial_detection_result/$*
