
/* Program AB Reference AIML 2.1 implementation
        Copyright (C) 2013 ALICE A.I. Foundation
        Contact: info@alicebot.org

        This library is free software; you can redistribute it and/or
        modify it under the terms of the GNU Library General Public
        License as published by the Free Software Foundation; either
        version 2 of the License, or (at your option) any later version.

        This library is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
        Library General Public License for more details.

        You should have received a copy of the GNU Library General Public
        License along with this library; if not, write to the
        Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
        Boston, MA  02110-1301, USA.
*/

import org.alicebot.ab.*;

import java.io.*;
import java.util.HashMap;


public class Main {

    public static void main (String[] args) {
        MagicStrings.setRootPath();

        AIMLProcessor.extension =  new PCAIMLProcessorExtension();
        mainFunction(args);
    }
    public static void mainFunction (String[] args) {
        String botName = "ryanreynolds";
        MagicBooleans.jp_tokenize = false;
        MagicBooleans.trace_mode = true;
        String action="chat";
    
        for (String s : args) {
            //System.out.println(s);
            String[] splitArg = s.split("=");
            if (splitArg.length >= 2) {
                String option = splitArg[0];
                String value = splitArg[1];
                //if (MagicBooleans.trace_mode) System.out.println(option+"='"+value+"'");
                if (option.equals("bot")) botName = value;
                if (option.equals("action")) action = value;
                if (option.equals("trace")) {
                    if (value.equals("true")) MagicBooleans.trace_mode = true;
                    else MagicBooleans.trace_mode = false;
                }
                if (option.equals("morph")) {
                    if (value.equals("true")) MagicBooleans.jp_tokenize = true;
                    else {
                        MagicBooleans.jp_tokenize = false;
                    }
                }
             }
        }
       // if (MagicBooleans.trace_mode) System.out.println("Working Directory = " + MagicStrings.root_path);
        Graphmaster.enableShortCuts = true;
        Graphmaster.DEBUG = false;
        //Timer timer = new Timer();
        Bot bot = new Bot(botName, MagicStrings.root_path, action); //
        //EnglishNumberToWords.makeSetMap(bot);
        //getGloss(bot, "c:/ab/data/wn30-lfs/wne-2006-12-06.xml");
        if (MagicBooleans.make_verbs_sets_maps) Verbs.makeVerbSetsMaps(bot);
        //bot.preProcessor.normalizeFile("c:/ab/data/log2.txt", "c:/ab/data/log2normal.txt");
        //System.exit(0);
        if (bot.brain.getCategories().size() < MagicNumbers.brain_print_size) bot.brain.printgraph();
        if (MagicBooleans.trace_mode) System.out.println("Action = '"+action+"'");
        if (action.equals("chat") || action.equals("chat-app")) {
			boolean doWrites = ! action.equals("chat-app");
			TestAB.testChat(bot, doWrites, MagicBooleans.trace_mode);
		}
        //else if (action.equals("test")) testSuite(bot, MagicStrings.root_path+"/data/find.txt");
        else if (action.equals("ab")) TestAB.testAB(bot, TestAB.sample_file);
        else if (action.equals("aiml2csv") || action.equals("csv2aiml")) convert(bot, action);
        else if (action.equals("abwq")){AB ab = new AB(bot, TestAB.sample_file);  ab.abwq();}
		else if (action.equals("test")) { TestAB.runTests(bot, MagicBooleans.trace_mode);     }
        else if (action.equals("shadow")) { MagicBooleans.trace_mode = false; bot.shadowChecker();}
        else if (action.equals("iqtest")) { ChatTest ct = new ChatTest(bot);
                try {
                    ct.testMultisentenceRespond();
                }
            catch (Exception ex) { ex.printStackTrace(); }
            }
        else System.out.println("Unrecognized action "+action);
    }
    public static void convert(Bot bot, String action) {
        if (action.equals("aiml2csv")) bot.writeAIMLIFFiles();
        else if (action.equals("csv2aiml")) bot.writeAIMLFiles();
    }
}

   