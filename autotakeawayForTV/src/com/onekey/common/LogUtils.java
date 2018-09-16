/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onekey.common;

import android.text.TextUtils;
import android.util.Log;

/**
 * Log工具，类似android.util.Log。
 * tag自动产生，格式: customTagPrefix:className.methodName(L:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(L:lineNumber)。
 * <p/>
 * Author: wyouflf
 * Date: 13-7-24
 * Time: 下午12:23
 */
public class LogUtils {

    public static String customTagPrefix = "";

    private LogUtils() {
    }

    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;
    
    public final static int MODEL_ALL = 1;
    public final static int MODEL_SHOW = 2;
    public final static int MODEL_ANNOTATION = 3;
    public final static int MODEL_PRACTICE = 4;
    
    public static boolean allowMODEL_ALL = false;
    public static boolean allowMODEL_SHOW = false;
    public static boolean allowMODEL_ANNOTATION = true;
    public static boolean allowMODEL_PRACTICE = false;
    
    private static String TAG = null;
    public static void setTag(String tag) {
    	TAG = tag;
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "[%s.%s(L:%d)]";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void model_d(int modelId, String content) {
    	boolean allow = false;
    	switch (modelId) {
		case LogUtils.MODEL_ALL:
			if (allowMODEL_ALL)
				allow = true;
			break;
		case LogUtils.MODEL_ANNOTATION:
			if (allowMODEL_ANNOTATION)
				allow = true;
			break;
		case LogUtils.MODEL_PRACTICE:
			if (allowMODEL_PRACTICE)
				allow = true;
			break;
		case LogUtils.MODEL_SHOW:
			if (allowMODEL_SHOW)
				allow = true;
			break;

		default:
			break;
		}
    	
    	if (!allow)
    		return;
    	
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.d(TAG, tag + " " + content);
        } else {
        	Log.d(tag, content);
        }
        
    }

    public static void d(String content) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.d(TAG, tag + " " + content);
        } else {
        	Log.d(tag, content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (!allowD) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.d(TAG, tag + " " + content, tr);
        } else {
        	Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        if (!allowE) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.e(TAG, tag + " " + content);
        } else {
        	Log.e(tag, content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!allowE) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.e(TAG, tag + " " + content, tr);
        } else {
        	Log.e(tag, content, tr);
        }
    }

    public static void i(String content) {
        if (!allowI) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.i(TAG, tag + " " + content);
        } else {
        	Log.i(tag, content);
        }
    }

    public static void i(String content, Throwable tr) {
        if (!allowI) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.i(TAG, tag + " " + content, tr);
        } else {
        	Log.i(tag, content, tr);
        }
    }

    public static void v(String content) {
        if (!allowV) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.v(TAG, tag + " " + content);
        } else {
        	Log.v(tag, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (!allowV) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.v(TAG, tag + " " + content, tr);
        } else {
        	Log.v(tag, content, tr);
        }
    }

    public static void w(String content) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.w(TAG, tag + " " + content);
        } else {
        	Log.w(tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.w(TAG, tag + " " + content, tr);
        } else {
        	Log.w(tag, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.w(TAG, tr);
        } else {
        	Log.w(tag, tr);
        }
    }


    public static void wtf(String content) {
        if (!allowWtf) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.wtf(TAG, tag + " " + content);
        } else {
        	Log.wtf(tag, content);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (!allowWtf) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.wtf(TAG, tag + " " + content, tr);
        } else {
        	Log.wtf(tag, content, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (!allowWtf) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (TAG != null) {
        	Log.wtf(TAG, tr);
        } else {
        	Log.wtf(tag, tr);
        }
    }
    
    /**
     * 将log信息写入文件中
     *
     * @param type
     * @param tag
     * @param msg
     */
    private static void writeToFile(char type, String tag, String msg) {

//        if (null == logPath) {
//            Log.e(TAG, "logPath == null ，未初始化LogToFile");
//            return;
//        }
//
//        String fileName = logPath + "/log_" + dateFormat.format(new Date()) + ".log";//log日志名，使用时间命名，保证不重复
//        String log = dateFormat.format(date) + " " + type + " " + tag + " " + msg + "\n";//log日志内容，可以自行定制
//
//        //如果父路径不存在
//        File file = new File(logPath);
//        if (!file.exists()) {
//            file.mkdirs();//创建父路径
//        }
//
//        FileOutputStream fos = null;//FileOutputStream会自动调用底层的close()方法，不用关闭
//        BufferedWriter bw = null;
//        try {
//
//            fos = new FileOutputStream(fileName, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
//            bw = new BufferedWriter(new OutputStreamWriter(fos));
//            bw.write(log);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bw != null) {
//                    bw.close();//关闭缓冲流
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }

    public static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[3];
    }

    public static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }
    
    public static void Printf(String format, Object... args) {
		StackTraceElement caller = getCallerStackTraceElement();
		String tag = generateTag(caller);

		String content = String.format(format, args);
		Log.d(tag, content);
	}
}
