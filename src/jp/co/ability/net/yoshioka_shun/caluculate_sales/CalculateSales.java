package jp.co.ability.net.yoshioka_shun.caluculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CalculateSales {


	public static void main (String args[]){
		// branchMapという名前のHashMapを用意する
		Map<String, String> branchMap = new HashMap<String, String>();
		//集計用のmapを作る
		Map<String, Integer> branchCalculateMap =new HashMap<String, Integer>();

		try{
			File file =new File(args[0],"branch.lst");
			FileReader fr =new FileReader(file);
			BufferedReader br = new BufferedReader(fr);


			String str;
			while((str = br.readLine()) != null){
				String[] items =str.split(",", 0);

				//ファイルフォーマットが不正の場合
				if(!items[0].matches("^\\d{3}$")){
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;
				}

				//branchMapにputする
				branchMap.put(items[0], items[1]);
				//集計mapにputする
				branchCalculateMap.put(items[0], 0);
			}
			br.close();
		} catch(IOException e){
			System.out.print("支店定義ファイルが存在しません");
			return;
		}



		try{
			File file =new File(args[0],"commodity.lst");
			FileReader fr =new FileReader(file);
			BufferedReader cr = new BufferedReader(fr);

			// commodityMapという名前のHashMapを用意する
			 Map<String,String> commodityMap = new HashMap<String,String>();

			String str;
			while((str = cr.readLine()) != null){
				 String[] items =str.split(",",0);
				// commodityMapにputする
				 commodityMap.put(items[0], items[1]);


			 //ファイルフォーマットが不正の場合
			 if(!items[0].matches("^\\w{8}$")){
				 System.out.println("商品定義ファイルのフォーマットが不正です");
				 return;
			 }
			 }


			cr.close();
			//商品定義ファイルが存在しない場合
		}catch(IOException e){
			 System.out.print("商品定義ファイルが存在しません");
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



//    	連番チェック(リストを回す)
//    	for(String str : rcdList) {
//
//    	}

    	// 集計をしていく(リストを回す)
    	for(String fileName : rcdList) {
        	//売上ファイル読み込み
        	try{
        		File file =new File(args[0], fileName);
        		FileReader fr =new FileReader(file);
        		BufferedReader er = new BufferedReader(fr);

        		//リスト作り
        		String str;
        		ArrayList<String> calculateList = new ArrayList<String>();
        		while((str = er.readLine()) != null){
        			calculateList.add(str);

        		}

        		calculateList.get(0);
        		calculateList.get(2);

        		//System.out.println(calculateList.get(0));
        		//System.out.println(branchCalculateMap.get(calculateList.get(0)));

        		int rcdVal = Integer.parseInt(calculateList.get(2));
        		int branchVal = branchCalculateMap.get(calculateList.get(0)) + rcdVal ;
        		branchCalculateMap.put(calculateList.get(0), branchVal );





//        		branchCalculateMap.put(calculateList.get(0),branchCalculateMap.get(calculateList.get(0)) + );
//        		System.out.println(branchCalculateMap.get(calculateList.get(0)));






        		er.close();

        		//売上ファイルが4桁以上ある場合
    		} catch (FileNotFoundException e) {


				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} finally {


    		}



    	}



	}
}




















