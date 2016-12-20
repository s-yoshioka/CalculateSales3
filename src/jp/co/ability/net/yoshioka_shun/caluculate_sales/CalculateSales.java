package jp.co.ability.net.yoshioka_shun.caluculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CalculateSales {

	public static void main (String args[]){
		// branchMapという名前のHashMapを用意する
		Map<String, String> branchMap = new HashMap<String, String>();
		//集計用のmapを作る(支店）
		Map<String, Long> branchCalculateMap =new HashMap<String, Long>();
		//集計用のmapを作る(商品)
		Map<String, Long> commodityCalculateMap = new HashMap<String,Long>();




		File file =new File(args[0],"branch.lst");
		FileReader fr =new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		try {


			String str;
			while((str = br.readLine()) != null){
				String[] items =str.split(",", 0);

				//ファイルフォーマットが不正の場合
				if(!items[0].matches("^\\d{3}$") || items.length !=2){
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;




				//branchMapにputする
				branchMap.put(items[0], items[1]);
				//集計map(支店)にputする
				branchCalculateMap.put(items[0], (long) 0);

					}
				}
			}finally{

				BufferedReader br = null;
				try{
					if(br = null){ br.close();}
				}catch(IOException e){
					System.out.println("支店定義ファイルが存在しません");
				}

		}

		// commodityMapという名前のHashMapを用意する
		 Map<String,String> commodityMap = new HashMap<String,String>();

		try {
			File file =new File(args[0],"commodity.lst");
			FileReader fr =new FileReader(file);
			BufferedReader cr = new BufferedReader(fr);

			String str;
			while((str = cr.readLine()) != null){
				 String[] items =str.split(",",0);



				 //ファイルフォーマットが不正の場合
				 if(!items[0].matches("^\\w{8}$") && (items.length !=2)){
					 System.out.println("商品定義ファイルのフォーマットが不正です");
					 return;
				 }


					// commodityMapにputする
				 commodityMap.put(items[0], items[1]);

				 commodityCalculateMap.put(items[0],(long) 0);



			}

			cr.close();
			//商品定義ファイルが存在しない場合
		} catch(IOException e){
			 System.out.println("商品定義ファイルが存在しません");
			 return;
		}



		// ディレクトリの一覧を取得する
    	File dir = new File(args[0]);
    	File[] files = dir.listFiles();

    	// ディレクトリの一覧からrcdファイルだけを抽出
    	ArrayList<String> rcdList = new ArrayList<String>();
    	for(int i= 0; i<files.length; i++){

    		// rcdの判定
    		if(files[i].isFile() && files[i].getName().matches("^\\d{8}.rcd$")){//8桁の条件追加
    			// リストに入れる
    			rcdList.add(files[i].getName());
    		}
    	}






    	//連番チェック(リストを回す)
    	for(int i = 0; i < rcdList.size() ; i++){
    		String rcdName = rcdList.get(i);
    		int num = Integer.parseInt(rcdName.substring(0, 8));
    		if(num - i != 1){
    			System.out.println("売上ファイル名が連番になっていません");
    			return;
    		}
    	}




    	// 集計をしていく(リストを回す)
    	for(String fileName : rcdList) {
			//売上ファイル読み込み
			try{
				File file =new File(args[0], fileName);
				FileReader fr =new FileReader(file);
				BufferedReader er = new BufferedReader(fr);
				//リスト作り
				String s;
				ArrayList<String> calculateList = new ArrayList<String>();
				while((s = er.readLine()) != null){
					calculateList.add(s);
				}

				//支店コードが不正です
				if(!branchCalculateMap.containsKey(calculateList.get(0))){
					System.out.println(fileName + "の支店コードが不正です");
					return;
				}

				//商品コードが不正です
				if(!commodityCalculateMap.containsKey(calculateList.get(1))){
					System.out.println(fileName + "の商品コードが不正です");
					return;
				}


				//売上ファイルが4行以上ある場合
				if(!(calculateList.size() ==3)){
					System.out.println(fileName + "のフォーマットが不正です");
					return;
        		}


				long rcdVal = Long.parseLong(calculateList.get(2));//売上額の数値化
				long branchVal = branchCalculateMap.get(calculateList.get(0)) + rcdVal ;//売上額(累計)の数値化

				long calVal = commodityCalculateMap.get(calculateList.get(1)) + rcdVal;


				branchCalculateMap.put(calculateList.get(0), branchVal );
				commodityCalculateMap.put(calculateList.get(1),calVal);





				//合計金額が10桁超えた場合
				if(branchVal > 1000000000 || calVal > 1000000000){

					System.out.println("合計金額が10桁超えました");
					return;
				}

				er.close();
        	}catch(IOException e){
    			System.out.println("");
    		}
    	}


    	// List 生成 (ソート用)（支店）
    	List<Entry<String,Long>> branchEntries =
				new ArrayList<Map.Entry<String,Long>>(branchCalculateMap.entrySet());
		Collections.sort(branchEntries, new Comparator<Map.Entry<String,Long>>() {

			public int compare(
					Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		// 内容を表示(支店コード・支店名・金額)
		for (Entry<String, Long> s : branchEntries) {
			System.out.println( s.getKey() + "," + branchMap.get(s.getKey())+"," + s.getValue() + System.getProperty("line.separator"));
		}

		// 出力処理
		try {
			File file = new File(args[0], "branch.out");
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for(Entry<String,Long> s : branchEntries) {
				bw.write(s.getKey() + "," + branchMap.get(s.getKey())+"," + s.getValue() + System.getProperty("line.separator"));
			}
			bw.close();

		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}

		// List 生成 (ソート用)（商品）
		List<Entry<String,Long>> commodityEntries =
				new ArrayList<Map.Entry<String,Long>>(commodityCalculateMap.entrySet());
		Collections.sort(commodityEntries, new Comparator<Map.Entry<String,Long>>() {

			public int compare(
					Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		 //内容を表示(商品コード・商品名・金額)
		for (Entry<String,Long> s : commodityEntries) {
			System.out.println( s.getKey() + "," + commodityMap.get(s.getKey())+"," + s.getValue() + System.getProperty("line.separator"));

		}

		//出力処理
		try{
			File file = new File(args[0],"commodity.out");
			FileWriter fw =new FileWriter(file);
			BufferedWriter bw  = new BufferedWriter(fw);
			for(Entry<String,Long> s : commodityEntries) {
				bw.write(s.getKey() + "," + commodityMap.get(s.getKey())+"," + s.getValue() + System.getProperty("line.separator"));
			}
			bw.close();

		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
	}
}







