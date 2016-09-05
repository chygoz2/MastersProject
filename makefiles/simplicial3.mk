JFLAGS = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 listInstances.txt)
RESULTSDIR := results
GRAPHDIR := generated_graphs

simplicial3: $(foreach i, $(INSTANCES), $(RESULTSDIR)/efficient_simplicial_listing_result/$(i))

$(RESULTSDIR)/efficient_simplicial_listing_result/%:
	mkdir -p $(RESULTSDIR)/efficient_simplicial_listing_result
	$(JVM) efficient.listing.ListSimplicialVertices $(GRAPHDIR)/$* | tee $(RESULTSDIR)/efficient_simplicial_listing_result/$*
