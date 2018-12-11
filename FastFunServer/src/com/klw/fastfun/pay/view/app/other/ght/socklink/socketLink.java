package com.klw.fastfun.pay.view.app.other.ght.socklink;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class socketLink 
{
	public Map<String, String> sendStr(String string, String incharset,String outcharset) {
		Socket s;
		try 
		{
			//s = new Socket("121.201.38.219",3191);
			s = new Socket("121.201.38.220",13191);
			
			Writer w=new OutputStreamWriter(s.getOutputStream(),incharset);
			w.write(string);
			w.flush();
			
			
			
			StringBuilder sd=new StringBuilder();
			Reader r= new InputStreamReader(s.getInputStream(),outcharset);
			char[] cbuf = new char[1024];
			int len;
			while((len=r.read(cbuf)) !=-1)
			{
				sd.append(new String(cbuf,0,len));
			}
			String sss=sd.toString();
			//System.out.printf("\n"+sss+"\n");
			w.close();
			r.close();
			
			s.close();
			
			sss=sss.substring(9,sss.length()-1);
			Map<String,String> ret=new HashMap<String,String>();
			String[] strs=sss.split(",");
			for(int i=0;i<strs.length;i++)
			{
				String[] st=strs[i].split("\"");
				if(st.length > 1)
					ret.put(st[1], (st.length<4 || st[3]==null)?"":st[3]);
			}
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//未来在线
	public Map<String, String> wlzxsendStr(String string) {
		Socket s;
		try 
		{
			s = new Socket("121.201.38.220",3391);
			
			Writer w=new OutputStreamWriter(s.getOutputStream());
			w.write(string);
			w.flush();
			
			
			
			StringBuilder sd=new StringBuilder();
			Reader r= new InputStreamReader(s.getInputStream(),"UTF-8");
			char[] cbuf = new char[1024];
			int len;
			while((len=r.read(cbuf)) !=-1)
			{
				sd.append(new String(cbuf,0,len));
			}
			String sss=sd.toString();
			//System.out.printf("\n"+sss+"\n");
			w.close();
			r.close();
			
			s.close();
			
			sss=sss.substring(9,sss.length()-1);
			Map<String,String> ret=new HashMap<String,String>();
			String[] strs=sss.split(",");
			for(int i=0;i<strs.length;i++)
			{
				String[] st=strs[i].split("\"");
				ret.put(st[1], (st.length<4 || st[3]==null)?"":st[3]);
			}
			return ret;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
