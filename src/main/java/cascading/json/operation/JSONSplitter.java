/*
 * Copyright 2009, Grégoire Marabout. All rights reserved.
 */

package cascading.json.operation;

import net.sf.json.JSON;
import net.sf.json.JSONNull;
import cascading.flow.FlowProcess;
import cascading.json.JSONUtils;
import cascading.json.JSONWritable;
import cascading.operation.Function;
import cascading.operation.FunctionCall;
import cascading.tuple.Fields;
import cascading.tuple.Tuple;

/**
 * @author <a href="mailto:gmarabout@gmail.com">Grégoire Marabout</a>
 */
public class JSONSplitter extends JSONOperation implements Function {

  private String[] paths;

  public JSONSplitter(Fields fieldDeclaration, String... paths){
    super( fieldDeclaration );
    this.paths = paths;
  }

  public void operate(FlowProcess flowProcess, FunctionCall functionCall){
	Object argument = functionCall.getArguments().getObject( 0 );
	JSON jsonObject = JSONUtils.getJSON(argument);
	
    Tuple output = new Tuple();
    for ( String path : paths ) {
      Comparable value = getValue( jsonObject, path );
      if (value instanceof JSON) {
    	  value = new JSONWritable((JSON) value);
      }
      else if (value == null) {
          value = new JSONWritable(JSONNull.getInstance());
      }
      output.add( value );
     
    }
    functionCall.getOutputCollector().add( output );
  }
}
