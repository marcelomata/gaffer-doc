${HEADER}

${CODE_LINK}

This example demonstrates how the [ReservoirItemsSketch<String>](https://github.com/DataSketches/sketches-core/blob/master/src/main/java/com/yahoo/sketches/sampling/ReservoirItemsSketch.java) sketch from the Data Sketches library can be used to maintain estimates of properties on vertices and edges. The ReservoirItemsSketch<String> sketch allows a sample of a set of strings to be maintained. We give two examples of this. The first is if when an edge is observed there is a string property associated to it, and there are a lot of different values of that string. We may not want to store all the different values of the string, but we may want to see a random sample of them. The second example is to store on an Entity a sketch which gives a sample of the vertices that are connected to the vertex. Even if we are storing all the edges then producing a random sample of the vertices attached to a vertex may not be quick (for example if a vertex has degree 10,000 then producing a sample of a random 10 neighbours would require scanning all the edges - storing the sketch on the Entity means that the sample will be precomputed and can be returned without scanning the edges).

##### Elements schema
This is our new elements schema. The edge has a property called 'stringsSample'. This will store the ReservoirItemsSketch<String> object. The entity has a property called 'neighboursSample'. This will also store a ReservoirItemsSketch<String> object.

${ELEMENTS_JSON}

##### Types schema
We have added a new type - 'reservoir.strings.sketch'. This is a [com.yahoo.sketches.sampling.ReservoirItemsSketch](https://github.com/DataSketches/sketches-core/blob/master/src/main/java/com/yahoo/sketches/sampling/ReservoirItemsSketch.java) object.
We also added in the [serialiser](https://github.com/gchq/Gaffer/blob/master/library/sketches-library/src/main/java/uk/gov/gchq/gaffer/sketches/datasketches/sampling/serialisation/ReservoirStringsSketchSerialiser.java) and [aggregator](https://github.com/gchq/Gaffer/blob/master/library/sketches-library/src/main/java/uk/gov/gchq/gaffer/sketches/datasketches/sampling/binaryoperator/ReservoirItemsSketchAggregator.java) for the ReservoirItemsSketch object. Gaffer will automatically aggregate these sketches, using the provided aggregator, so they will keep up to date as new edges are added to the graph.

${TYPES_JSON}

An edge A-B of group "red" was added to the graph 1000 times. Each time it had the stringsSample property containing a randomly generated string. Here is the edge:
```
${GET_A-B_EDGE_RESULT}
```

This is not very illuminating as this just shows the default `toString()` method on the sketch. To get value from it we need to call a method on the ReservoirItemsSketch object:

${START_JAVA_CODE}
${GET_SAMPLE_FOR_EDGE_A_B_SNIPPET}
${END_CODE}

The results contain a random sample of the strings added to the edge:
```
${GET_SAMPLE_FOR_RED_EDGE}
```

500 edges of group "blue" were also added to the graph (edges X-Y0, X-Y1, ..., X-Y499). For each of these edges, an Entity was created for both the source and destination. Each Entity contained a 'neighboursSample' property that contains the vertex at the other end of the edge. We now get the Entity for the vertex X and display the sample of its neighbours:

${START_JAVA_CODE}
${GET_SAMPLE_FOR_ENTITY_X_SNIPPET}
${END_CODE}

The results are:

```
${GET_SAMPLES_FOR_X_RESULT}
```
