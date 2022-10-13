package tw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TWData {

	//エンチャント一覧
	//[0]エンチャントの名前
	//[1]レベル
	//[2]グレード
	//[3]対象
	//1:頭 2:体 4:ズボン 8:靴 16:剣 32:弓 64:クロスボウ 128：トライデント 256:釣り竿
	//512:盾 1024:食料 2048:鉱石類 4096:ツルハシ 8192：斧 16384:クワ 32768:船 65536:卵 131072:それ以外全て
	//262143:なんでもOK
	//133120:耐久力(卵)以外の全てのアイテム
	//15：防具類
	//511:耐久力がある者
	//TODO ショップの存在
	public static Object[][] enchant = {
			{"水中歩行",1,1,8},{"水中歩行",2,2,8},{"水中歩行",3,3,8},
			{"爆発耐性",1,0,15},{"爆発耐性",2,1,15},{"爆発耐性",3,2,15},{"爆発耐性",4,3,15},
			{"落下耐性",1,1,15},{"落下耐性",2,2,15},{"落下耐性",3,3,15},{"落下耐性",4,5,15},
			{"火炎耐性",1,0,15},{"火炎耐性",2,1,15},{"火炎耐性",3,2,15},{"火炎耐性",4,3,15},
			{"宝釣り",1,1,256},{"宝釣り",2,2,256},{"宝釣り",3,3,256},
			{"入れ食い",1,1,256},{"入れ食い",2,2,256},{"入れ食い",3,3,256},
			{"射撃ダメージ増加",1,2,32},{"射撃ダメージ増加",2,3,32},{"射撃ダメージ増加",3,5,32},
			{"ダメージ軽減",1,1,15},{"ダメージ軽減",2,2,15},{"ダメージ軽減",3,3,15},{"ダメージ軽減",4,5,15},
			{"ダメージ増加",1,2,16},{"ダメージ増加",2,3,16},{"ダメージ増加",3,5,16},
			{"棘の鎧",1,1,15},{"棘の鎧",2,2,15},{"棘の鎧",3,3,15},
			{"耐久力",1,1,262143},{"耐久力",2,2,262143},{"耐久力",3,3,262143},
			{"飛び道具耐性",1,1,15},{"飛び道具耐性",2,2,15},{"飛び道具耐性",3,3,15},{"飛び道具耐性",4,5,15},
			{"ノックバック",1,1,262143},{"ノックバック",2,2,262143},
			{"パンチ",1,1,32},{"パンチ",2,2,32},
			{"範囲ダメージ増加",1,1,16},{"範囲ダメージ増加",2,2,16},
			{"火属性",1,2,16},{"火属性",2,3,16},
			{"高速装填",1,2,64},{"高速装填",2,3,64},{"高速装填",3,5,64},
			{"水中呼吸",1,2,1},{"水中呼吸",2,3,1},{"水中呼吸",3,5,1},
			{"貫通",1,2,64},
			{"フレイム",1,3,32},
			{"召雷",1,3,128},
			{"忠誠",1,3,128},
			{"拡散",1,3,64},
			{"氷渡り",1,4,8},
			{"無限",1,4,32},
			{"激流",1,4,128},
			{"束縛の呪い",1,1,15},
			{"吸血",1,3,16},{"吸血",2,4,16},
			{"浮遊",1,2,16},{"浮遊",2,3,16},{"浮遊",5,5,16},
			{"固定火力",1,3,262143},{"固定火力",2,5,262143},
			{"連撃",1,2,32},{"連撃",2,3,32},{"連撃",3,5,32},
			{"免疫",1,3,15},
			{"便乗",1,4,32},
			{"帰還",1,3,133120},
			{"ロケット",1,2,133120},{"ロケット",2,3,133120},
			{"反動",1,1,16},{"反動",2,2,16},{"反動",3,3,16},
			{"爆竹",1,1,16},{"爆竹",2,2,16},
			{"ブラックホール",1,2,2},{"ブラックホール",2,3,2},{"ブラックホール",3,5,2},
			{"利口",1,0,1},{"利口",2,1,1},{"利口",3,2,1},{"利口",4,3,1},{"利口",5,5,1},
			{"流出",1,2,16},{"流出",2,3,16},{"流出",3,5,16},
			{"学習",1,0,1},{"学習",2,1,1},{"学習",3,2,1},{"学習",4,3,1},{"学習",5,5,1},
			{"透明",1,1,8},
			{"消失",1,3,2},
			{"節約",1,0,1},{"節約",2,1,1},{"節約",3,2,1},{"節約",4,3,1},{"節約",5,5,1},
			{"崩壊",1,3,16},{"崩壊",2,5,16},
			{"再生体",1,2,2},{"再生体",2,3,2},{"再生体",3,5,2},
			{"軽量化",1,1,4},{"軽量化",2,2,4},{"軽量化",3,3,4},{"軽量化",4,5,4},
			{"保管空間",1,2,133120},
			{"瓦割り",1,3,8192},
			{"水性",1,1,2},{"水性",2,2,2},{"水性",3,3,2},{"水性",4,5,2},
			{"模造",1,3,133120},
			{"禁忌",1,4,16},
			{"金槌",1,1,2},{"金槌",2,2,2},{"金槌",3,3,2},
			{"重り",1,2,16},
			{"不幸中の幸い",1,4,1},
			{"銅鑼",1,5,2},
			{"晴れの祈り",1,3,133120},
			{"雨の祈り",1,3,133120},
			{"雷の祈り",1,3,133120},
			{"皿",1,3,1},
			{"光合成",1,3,2},
			{"着付け",1,4,16},
			{"乾燥肌",1,1,2},
			{"偏頭痛",1,1,1},
			{"びびり腰",1,1,4},
			{"百鬼夜行",1,3,2},
			{"王冠",1,4,1},
			{"生還",1,3,133120},
			{"洗脳",1,3,1},
			{"黒魔術",1,2,2},{"黒魔術",2,3,2},
			{"生物薬品",1,1,133120},{"生物薬品",2,2,133120},{"生物薬品",3,3,133120},
			{"ハミング",1,1,8},{"ハミング",2,2,8},{"ハミング",3,3,8},
			{"宿り木",1,2,4},{"宿り木",2,3,4},
			{"適応性",1,3,32},
			{"検閲",1,3,133120},
			{"千里眼",1,5,133120},
			{"末梢",1,3,133120},
			{"足すくい",1,5,133120},
			{"息吹",1,1,133120},{"息吹",2,2,133120},{"息吹",3,3,133120},
			{"プレゼント",1,4,16},
			{"炭鉱夫",1,1,1},{"炭鉱夫",2,2,1},{"炭鉱夫",3,3,1},
			{"鑑定士",1,5,1},
			{"こだわり",1,1,1},{"こだわり",2,2,1},{"こだわり",3,3,1},{"こだわり",4,5,1},
			{"ラッキーパンチ",1,3,8192},
			{"降臨",1,3,133120},
			{"間一髪",1,3,4}
	};
	
	public static Object[][] discription = {
			{"水中歩行","水中で歩く速度が上昇",3},
			{"爆発耐性","爆発によるダメージが低下",4},
			{"落下耐性","落下によるダメージが低下",4},
			{"火炎耐性","炎によるダメージが低下",4},
			{"宝釣り","レアな物を釣る確率が上昇",3},
			{"入れ食い","釣れる確率が上昇",3},
			{"射撃ダメージ増加","矢のダメージが増加",3},
			{"ダメージ軽減","食らうダメージが低下",4},
			{"ダメージ増加","与えるダメージが増加",3},
			{"棘の鎧","ダメージを少し反射する",3},
			{"耐久力","壊れにくくなる",3},
			{"飛び道具耐性","飛び道具によるダメージが低下",4},
			{"ノックバック","攻撃した相手を強く飛ばす",2},
			{"パンチ","矢が当たった相手を強く飛ばす",2},
			{"範囲ダメージ増加","範囲攻撃のダメージが上昇",2},
			{"火属性","攻撃した相手に火がつく",2},
			{"高速装填","速く矢をセットできる",3},
			{"水中呼吸","水中で息が長く続く",3},
			{"貫通","矢が敵を貫通する",1},
			{"フレイム","矢が命中した敵が燃える",1},
			{"召雷","落ちた場所に雷が降る",1},
			{"忠誠","投げた後、戻ってくる",1},
			{"拡散","矢が3本に増える",1},
			{"氷渡り","水の上を歩ける",1},
			{"無限","矢を消費しなくなる",1},
			{"激流","水や雨の中で勢い良く動ける",1},
			{"束縛の呪い","装備が脱げなくなる",1},
			{"吸血","攻撃する度、回復する",2},
			{"浮遊","攻撃した相手が浮遊する",5},
			{"固定火力","一定のダメージが確実に入る",2},
			{"連撃","矢が連続で発射される",3},
			{"免疫","ポーション効果を食らわない",1},
			{"便乗","放った矢に乗る",1},
			{"帰還","右クリックで拠点に帰れるが、最大体力が減少",1},
			{"ロケット","右クリックで飛ぶ",2},
			{"反動","攻撃すると、自分が飛ぶ",3},
			{"爆竹","時間差で相手にもう一撃入る",3},
			{"ブラックホール","スニークで敵を引き寄せる",3},
			{"利口","経験値が多く手に入る",5},
			{"流出","攻撃した相手のレベルを下げる",3},
			{"学習","攻撃したとき、たまにレベルが上がる",5},
			{"透明","透明になる",1},
			{"消失","壊れる代わりに攻撃してきた相手の手持ちを消す",1},
			{"節約","たまにレベルを消費しない",5},
			{"崩壊","自分と相手に追加でダメージが発生する",2},
			{"再生体","ダメージを食らってから一定時間後、回復する",3},
			{"軽量化","足が速くなる",4},
			{"保管空間","専用のインベントリを開ける",1},
			{"瓦割り","壊れる代わりに攻撃した相手の装備を壊す",1},
			{"水性","水の中にいる時、たまに回復する",4},
			{"模造","攻撃した相手の持ってる物になる",1},
			{"禁忌","レベルを消費して火力を上げる",1},
			{"金槌","周りのプレイヤーは泳げなくなる",3},
			{"重り","相手を強く下に叩きつける",1},
			{"不幸中の幸い","ダメージを受けた時、たまに宝箱を得る",1},
			{"銅鑼","攻撃してきた相手は手持ちを落す",1},
			{"晴れの祈り","右クリックで消費して晴れにする",1},
			{"雨の祈り","右クリックで消費して雨にする",1},
			{"雷の祈り","右クリックで消費して雷雨にする",1},
			{"皿","雨か雷雨時、移動速度上昇",1},
			{"光合成","晴れの時、回復速度が上がる",1},
			{"着付け","自分の装備を全部相手に付け替える",1},
			{"乾燥肌","食料ゲージが徐々に下がる",1},
			{"偏頭痛","たまにダメージを食らう",1},
			{"びびり腰","攻撃を食らうとスローになる",1},
			{"百鬼夜行","攻撃を食らう度、ゾンビが湧く",1},
			{"王冠","モンスターから狙われなくなる",1},
			{"生還","13レベル消費して帰還する",1},
			{"洗脳","自分を攻撃したモンスターは味方になる",1},
			{"黒魔術","近くで生物が倒れる度に回復する",2},
			{"生物薬品","右クリックした生物が回復する",3},
			{"ハミング","走ってる間、回復速度が上がる",3},
			{"宿り木","草土の上にいる間、回復速度が上がる",2},
			{"適応性","天候によって矢に特殊効果が付く",1},
			{"検閲","右クリックで消費して誰かの手に持ってる物を見る",1},
			{"千里眼","右クリックで消費して全員発光",1},
			{"末梢","右クリックした生物を消すが、最大体力が減る",1},
			{"足すくい","右クリックして全員の足装備破壊",1},
			{"息吹","右クリックで近くの敵を吹き飛ばす",3},
			{"プレゼント","攻撃した相手にリンゴを渡す",1},
			{"炭鉱夫","宝探しが成功しやすくなる",1},
			{"鑑定士","宝が3択になる",1},
			{"こだわり","指定したレベルしかエンチャント出来ない",1},
			{"ラッキーパンチ","攻撃した時、一定確率で宝箱",1},
			{"降臨","ランダムな船に予告してからテレポート",1},
			{"間一髪","倒れそうなとき、全て失って逃げる",1}
	};
	
	//レベルアップに必要なレベル、0,1,2,3
	//[0]作業台
	//[1]矢細工台
	//[2]製図台
	//[3]エンチャント台
	//[4]鍛冶台
	public static int[][] requireLevels = {
			{0,2,4,6},
			{2,4,6,8},
			{2,4,6,8},
			{2,4,6,8},
			{2,4,6,8}
	};
	
	public static Enchantment getRealEnchantment(String name) {
		switch(name) {
		case "水中歩行":
			return Enchantment.DEPTH_STRIDER;
		case "爆発耐性":
			return Enchantment.PROTECTION_EXPLOSIONS;
		case "落下耐性":
			return Enchantment.PROTECTION_FALL;
		case "火炎耐性":
			return Enchantment.PROTECTION_FIRE;
		case "幸運":
			return Enchantment.LUCK;
		case "入れ食い":
			return Enchantment.LURE;
		case "射撃ダメージ増加":
			return Enchantment.ARROW_DAMAGE;
		case "ダメージ軽減":
			return Enchantment.PROTECTION_ENVIRONMENTAL;
		case "ダメージ増加":
			return Enchantment.DAMAGE_ALL;
		case "棘の鎧":
			return Enchantment.THORNS;
		case "耐久力":
			return Enchantment.DURABILITY;
		case "飛び道具耐性":
			return Enchantment.PROTECTION_PROJECTILE;
		case "ノックバック":
			return Enchantment.KNOCKBACK;
		case "パンチ":
			return Enchantment.ARROW_KNOCKBACK;
		case "範囲ダメージ増加":
			return Enchantment.SWEEPING_EDGE;
		case "火属性":
			return Enchantment.FIRE_ASPECT;
		case "高速装填":
			return Enchantment.QUICK_CHARGE;
		case "水中呼吸":
			return Enchantment.OXYGEN;
		case "貫通":
			return Enchantment.PIERCING;
		case "フレイム":
			return Enchantment.ARROW_FIRE;
		case "召雷":
			return Enchantment.CHANNELING;
		case "忠誠":
			return Enchantment.LOYALTY;
		case "拡散":
			return Enchantment.MULTISHOT;
		case "氷渡り":
			return Enchantment.FROST_WALKER;
		case "無限":
			return Enchantment.ARROW_INFINITE;
		case "激流":
			return Enchantment.RIPTIDE;
		case "束縛の呪い":
			return Enchantment.BINDING_CURSE;
		default:
			return null;
		}
	}
	
	//船員
	//[0]見た目
	//[1]名前
	//[2]タイプ
	//[3]値段
	//[4]説明
	public static Object[][] shipmates = {
			{Material.ROTTEN_FLESH,"ゾンビ",mateTypes.Active,3,"日に弱く、燃える。"},
			{Material.BONE,"スケルトン",mateTypes.Stay,4,"日に弱く、燃える。"},
			{Material.SAND,"ハスク",mateTypes.Active,8,"日によって、燃えない。"},
			{Material.BOW,"ストレイ",mateTypes.Stay,6,"日に弱く、燃える。"},
			{Material.ENDER_PEARL,"エンダーマン",mateTypes.Free,16,"現れた途端、誰かを敵対する。"},
			{Material.SLIME_BALL,"スライム",mateTypes.Stay,6,"大きさは買った時のお楽しみ。"},
			{Material.MAGMA_CREAM,"マグマキューブ",mateTypes.Invader,9,"ランダムな敵の船に現れる。"}
	};
	
	public static enum mateTypes {Active,Stay,Invader,Free};
	
	public static String getMateTypesName(mateTypes m) {
		switch(m) {
		case Active:
			return "アクティブ";
		case Stay:
			return "ステイ";
		case Invader:
			return "インベーダー";
		default:
			return "フリー";
		}
	}
	
	//トラップ一覧
	//[0]見た目
	//[1]名前
	//[2]値段
	//[3]説明
	public static Object[][] traps = {
			{Material.ENDER_EYE,"アラートトラップ",3,"侵入者をメッセージで知らせる"},
			{Material.RED_DYE,"ファイアトラップ",5,"相手を強い火で焼き尽くす"},
			{Material.NETHER_STAR,"フラッシュトラップ",7,"相手を長時間発光させる"},
			{Material.FERMENTED_SPIDER_EYE,"ポイズントラップ",13,"相手を強い毒にさらす"},
			{Material.IRON_AXE,"ブレイクトラップ",23,"相手の装備を壊す"}
	};
	
	//鍛冶台の品揃え
	//[0]商品の見た目
	//[1]数
	//[2]値段
	//[3]グレード
	//[4]カテゴリー
	////0:武器系
	////1:装備系
	////2:食料系
	////3:媒体系
	////4:その他
	//([5]アイテムの説明)
	//([6]アイテム名)
	public static Object[][] shopitems = {
			{Material.WOODEN_SWORD,1,3,0,0},
			{Material.STONE_SWORD,1,6,1,0},
			{Material.IRON_SWORD,1,11,2,0},
			{Material.DIAMOND_SWORD,1,17,3,0},
			{Material.WOODEN_AXE,1,14,2,0},
			{Material.BOW,1,8,1,0},
			{Material.CROSSBOW,1,14,3,0},
			{Material.ARROW,3,3,1,0},
			{Material.FISHING_ROD,1,4,0,4},
			{Material.SHIELD,1,10,2,0},
			{Material.IRON_PICKAXE,1,5,0,4},
			{Material.LEATHER_HELMET,1,1,0,1},
			{Material.LEATHER_CHESTPLATE,1,5,0,1},
			{Material.LEATHER_LEGGINGS,1,2,0,1},
			{Material.LEATHER_BOOTS,1,3,0,1},
			{Material.CHAINMAIL_HELMET,1,2,1,1},
			{Material.CHAINMAIL_CHESTPLATE,1,6,1,1},
			{Material.CHAINMAIL_LEGGINGS,1,3,1,1},
			{Material.CHAINMAIL_BOOTS,1,4,1,1},
			{Material.IRON_HELMET,1,5,2,1},
			{Material.IRON_CHESTPLATE,1,9,2,1},
			{Material.IRON_LEGGINGS,1,6,2,1},
			{Material.IRON_BOOTS,1,7,2,1},
			{Material.DIAMOND_HELMET,1,7,3,1},
			{Material.DIAMOND_CHESTPLATE,1,11,3,1},
			{Material.DIAMOND_LEGGINGS,1,8,3,1},
			{Material.DIAMOND_BOOTS,1,9,3,1},
			{Material.APPLE,2,1,0,2},
			{Material.BREAD,1,2,1,2},
			{Material.COOKED_BEEF,1,4,2,2},
			{Material.GOLDEN_CARROT,1,4,3,2},
			{Material.STICK,2,1,0,3},
			{Material.IRON_INGOT,1,2,1,3,"使用後、一定時間、移動速度が上昇する。"},
			{Material.GOLD_INGOT,1,2,1,3,"使用後、一定時間、跳躍力が上昇する。"},
			{Material.REDSTONE,1,4,2,3,"使用後、一定時間、耐性が上昇する。"},
			{Material.DIAMOND,1,6,3,3,"使用後、一定時間、攻撃力が上昇する。"},
			{Material.EMERALD,1,6,3,3,"使用後、一定時間、再生能力が上昇する。"},
			{Material.AMETHYST_SHARD,1,8,3,3,"使用後、一定時間、ランダムなバフポーション効果が2つ付与される。"},
			{Material.TRAPPED_CHEST,1,35,3,4,"右クリックでランダムに宝物が手に入る。","宝箱"},
			{Material.EXPERIENCE_BOTTLE,1,2,3,4,"使用すると、1レベル手に入れる。","レベルボトル"},
			{Material.TNT,1,23,3,4,"使用すると、足元に起爆したTNTが設置される。"}
	};
	public static Object[][] treasureitems = {
			{Material.TRIDENT,1},
			{Material.GOLDEN_APPLE,2},
			{Material.ENCHANTED_GOLDEN_APPLE,1},
			{Material.NETHERITE_INGOT,1,"使用後、体力が全回復する。"},
			{Material.ELYTRA,1}
	};
	public static Object[][] lizashop = {
			{Material.TOTEM_OF_UNDYING,1,35},
			{Material.GOLDEN_APPLE,1,15},
			{Material.ENCHANTED_GOLDEN_APPLE,1,35},
			{Material.NETHERITE_SWORD,1,23},
			{Material.NETHERITE_HELMET,1,12},
			{Material.NETHERITE_CHESTPLATE,1,17},
			{Material.NETHERITE_LEGGINGS,1,13},
			{Material.NETHERITE_BOOTS,1,15},
			{Material.IRON_PICKAXE,1,9}
	};
	
	public static List<ItemStack> getShopItems(int level,int category) {
		List<ItemStack> result = new ArrayList<ItemStack>();
		for(int i = 0 ; i < shopitems.length ; i++) {
			if((int)shopitems[i][3] <= level && (int)shopitems[i][4] == category) {
				ItemStack item = new ItemStack((Material)shopitems[i][0],(int)shopitems[i][1]);
				ItemMeta itemm = item.getItemMeta();
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GOLD + "値段：" + (int)shopitems[i][2] + "レベル");
				//補足説明がある場合
				if(shopitems[i].length >= 6) {
					StringBuilder sb = new StringBuilder();
					char[] chars = ((String)shopitems[i][5]).toCharArray();
					for(char c:chars) {
						sb.append(c);
						if(sb.toString().length() >= 15) {
							lore.add(ChatColor.WHITE + sb.toString());
							sb = new StringBuilder();
						}
					}
					if(sb.length() >= 1) {
						lore.add(ChatColor.WHITE + sb.toString());
					}
					//アイテム名が決められている場合
					if(shopitems[i].length >= 7) {
						itemm.setDisplayName((String)shopitems[i][6]);
					}
				}
				itemm.setLore(lore);
				item.setItemMeta(itemm);
				result.add(item);
			}
		}
		return result;
	}
	
	public static List<ItemStack> getShopItemsLiza(){
		List<ItemStack> result = new ArrayList<ItemStack>();
		for(int i = 0 ; i < lizashop.length ; i++) {
			ItemStack item = new ItemStack((Material)lizashop[i][0],(int)lizashop[i][1]);
			ItemMeta itemm = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GOLD + "値段：" + (int)lizashop[i][2] + "レベル");
			//補足説明がある場合
			if(lizashop[i].length >= 4) {
				StringBuilder sb = new StringBuilder();
				char[] chars = ((String)lizashop[i][3]).toCharArray();
				for(char c:chars) {
					sb.append(c);
					if(sb.toString().length() >= 15) {
						lore.add(ChatColor.WHITE + sb.toString());
						sb = new StringBuilder();
					}
				}
				if(sb.length() >= 1) {
					lore.add(ChatColor.WHITE + sb.toString());
				}
				//アイテム名が決められている場合
				if(lizashop[i].length >= 5) {
					itemm.setDisplayName((String)lizashop[i][4]);
				}
			}
			itemm.setLore(lore);
			item.setItemMeta(itemm);
			result.add(item);
		}
		return result;
	}
	
	public static List<ItemStack> getBookLiza(){
		List<ItemStack> result = new ArrayList<ItemStack>();
		for(int i = 0 ; i < enchant.length ; i++) {
			if((int)enchant[i][2] == 5) {
				ItemStack openbook = new ItemStack(Material.BOOK);
				ItemMeta openbookm = openbook.getItemMeta();
				openbookm.setDisplayName((String)enchant[i][0]);
				openbookm.setLore(new ArrayList<String>(Arrays.asList(ChatColor.GREEN + "Lv." + (int)enchant[i][1] )));
				openbook.setItemMeta(openbookm);
				result.add(openbook);
			}
		}
		return result;
	}
	
	public static ItemStack getTreasureBox() {
		ItemStack item = new ItemStack(Material.TRAPPED_CHEST);
		ItemMeta itemm = item.getItemMeta();
		itemm.setDisplayName("宝箱");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.WHITE + "右クリックでランダムに宝物が手");
		lore.add(ChatColor.WHITE + "に入る。");
		itemm.setLore(lore);
		item.setItemMeta(itemm);
		return item;
	}
	
	public static int getShopItemPrice(Material m) {
		for(int i = 0 ; i < shopitems.length ; i++) {
			if((Material)shopitems[i][0] == m) {
				return (int)shopitems[i][2];
			}
		}
		return 999;
	}
	
	public static int getShopItemPriceLiza(Material m) {
		for(int i = 0 ; i < lizashop.length ;i++) {
			if((Material)lizashop[i][0] == m) {
				return (int)lizashop[i][2];
			}
		}
		return 999;
	}
	
	public static String getDiscription(String s) {
		for(int i = 0 ; i < discription.length ; i++) {
			if(((String)discription[i][0]).equals(s)) {
				return (String)discription[i][1];
			}
		}
		return "";
	}
	
	public static int getMaxLevel(String s) {
		for(int i = 0 ; i < discription.length ; i++) {
			if(((String)discription[i][0]).equals(s)) {
				return (int)discription[i][2];
			}
		}
		return -1;
	}
	
	public static ItemStack getTreasure() {
		Random rnd = new Random();
		int rndm = rnd.nextInt(6);
		if(rndm <= 3) {
			//新たなエンチャント
			List<Object[]> ne = new ArrayList<Object[]>();
			for(int i = 0 ; i < enchant.length ; i++) {
				if((int)enchant[i][2] == 4) {
					ne.add(enchant[i]);
				}
			}
			rndm = rnd.nextInt(ne.size());
			ItemStack book = new ItemStack(Material.BOOK);
			ItemMeta bookm = book.getItemMeta();
			bookm.setDisplayName((String)ne.get(rndm)[0]);
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GREEN + "Lv." + (int)ne.get(rndm)[1]);
			lore.add(ChatColor.YELLOW + "右クリックで解法出来るよ！");
			bookm.setLore(lore);
			book.setItemMeta(bookm);
			return book;
		}else {
			//新たなアイテム
			List<ItemStack> treasures = new ArrayList<ItemStack>();
			for(int i = 0 ; i < treasureitems.length ; i++) {
				ItemStack treasure = new ItemStack((Material)treasureitems[i][0],(int)treasureitems[i][1]);
				if(treasureitems[i].length >= 3) {
					ItemMeta treasurem = treasure.getItemMeta();
					List<String> lore = new ArrayList<String>();
					StringBuilder sb = new StringBuilder();
					char[] cs = ((String)treasureitems[i][2]).toCharArray();
					for(char c:cs) {
						sb.append(c);
						if(sb.toString().length() >= 15) {
							lore.add(ChatColor.WHITE + sb.toString());
							sb = new StringBuilder();
						}
					}
					if(sb.toString().length() >= 1) {
						lore.add(ChatColor.WHITE + sb.toString());
					}
					treasurem.setLore(lore);
					treasure.setItemMeta(treasurem);
				}
				treasures.add(treasure);
			}
			rndm = rnd.nextInt(treasures.size());
			return treasures.get(rndm);
		}
	}
	
	public static ItemStack getBook(String enchantname,int level) {
		ItemStack book = new ItemStack(Material.BOOK);
		ItemMeta bookm = book.getItemMeta();
		bookm.setDisplayName(enchantname);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GREEN + "Lv." + level);
		lore.add(ChatColor.YELLOW + "右クリックで解法出来るよ！");
		bookm.setLore(lore);
		book.setItemMeta(bookm);
		return book;
	}
}
