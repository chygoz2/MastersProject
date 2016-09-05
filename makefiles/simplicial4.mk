JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 listInstances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

simplicial4: $(foreach i, $(INSTANCES), $(RESULTSDIR)/bruteforce_simplicial_listing_result/$(i))

$(RESULTSDIR)/bruteforce_simplicial_listing_result/%:
	mkdir -p $(RESULTSDIR)/bruteforce_simplicial_listing_result
	$(JVM) bruteforce.listing.ListSimplicialVertices $(GRAPHDIR)/$* | tee $(RESULTSDIR)/bruteforce_simplicial_listing_result/$*
