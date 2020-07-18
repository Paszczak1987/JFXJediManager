package jedi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class FileCodder {

	private static final String alphabet;
	private static List<Character> symbols;
	private static String key;

	static {
		alphabet = "0123456789Aa•πBbCcDdEe ÍFfGgHhIiJjKkLl£≥MmNn—ÒOo”ÛPpQqRrSsåúTtUuVvWwXxYyZzèüØø -_()";
		symbols = new ArrayList<Character>();
		for (int i = 0; i < alphabet.length(); i++)
			symbols.add(alphabet.charAt(i));
		key = null;
	}

	private static void makeKey() {
		Collections.shuffle(symbols);
		StringBuilder keyBuild = new StringBuilder();
		for (char ch : symbols)
			keyBuild.append(ch);
		key = keyBuild.toString();
	}

	private static String encodeLine(String line) {
		StringBuilder code = new StringBuilder();
		for (int lineItr = 0; lineItr < line.length(); lineItr++) {
			for (int alphabetItr = 0; alphabetItr < alphabet.length(); alphabetItr++) {
				if (line.charAt(lineItr) == alphabet.charAt(alphabetItr)) {
					code.append(key.charAt(alphabetItr));
				}
			}
		}
		return code.toString();
	}

	private static String decodeLine(String codedLine, String key) {
		StringBuilder decodedLine = new StringBuilder();
		for (int codeItr = 0; codeItr < codedLine.length(); codeItr++) {
			for (int keyItr = 0; keyItr < key.length(); keyItr++) {
				if (codedLine.charAt(codeItr) == key.charAt(keyItr)) {
					decodedLine.append(alphabet.charAt(keyItr));
				}
			}
		}
		return decodedLine.toString();
	}
	
	public static void encodeToFile(String url, List<String> messages) {
		makeKey();
		PrintWriter fileWriter = null;
		try {
			fileWriter = new PrintWriter(url);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		fileWriter.println("*"+key);
		for(String line : messages)
			fileWriter.println(encodeLine(line));
		fileWriter.close();
	}
	
	public static void decodeFromFile(String url, List<String> decodedMessages){
		String key = null;
		File inputFile = new File(url);
		Scanner inputFileReader = null;
		try {
			inputFileReader = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		while(inputFileReader.hasNextLine()) {
			String line = inputFileReader.nextLine();
			if(line.charAt(0) == '*')
				key = line.substring(1);
			else
				decodedMessages.add(decodeLine(line, key));
		}
		inputFileReader.close();
	}

}// class FileCodder
