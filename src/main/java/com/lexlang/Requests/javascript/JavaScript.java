package com.lexlang.Requests.javascript;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
* @author lexlang
* @version 2019年4月29日 上午9:50:59
* 
*/
public class JavaScript {
	private Invocable invocable;
	
	/**
	 * 
	 * @param js 对应的js函数
	 */
	public JavaScript(String js){
		invocable=getInvocable(js);
	}
	
	/**
	 * 
	 * @param name 函数名
	 * @param args 参数
	 * @return
	 * @throws NoSuchMethodException
	 * @throws ScriptException
	 */
	public String invokeFunction(String name, Object... args) throws NoSuchMethodException, ScriptException{
		Object result = invocable.invokeFunction(name, args);
		return result.toString();
	}
	
    public Invocable getInvocable(String js) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            engine.eval(js);
            if (engine instanceof Invocable) {
                Invocable invocable = (Invocable) engine;
                return invocable;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
