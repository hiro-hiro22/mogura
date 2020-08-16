package Mogmog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * パネルクラス
 */
public class MGPanel extends JPanel {

	//フェーズ
	private int phase = 0;

	//マウスアダプタ
	private MGMouseAdapter mgma = null;

	//ピコピコハンマー座標
	private int px = 0;
	//ピコピコハンマー座標
	private int py = 0;

	//もぐら座標
	private int mx = 350;
	//もぐら座標
	private int my = 200;

	// 背景
	private BufferedImage imageBackground = null;

	//ピコピコハンマー
	private BufferedImage[] imagePHs = null;
	// ピコピコハンマー状態
	private int ph = 0;

	// もぐら
	private BufferedImage[] imageMs = null;
	//もぐら状態
	private int m = 0;
	//もぐらの時間
	private int timeM = 0;

	//タイマー
	private java.util.Timer timerThis = null;

	// 得点
	private int score = 0;
	private Font fontScore = new Font("ＭＳ ゴシック", Font.BOLD | Font.ITALIC, 24);

	// 制限時間
	private int time = 1859;
	private Font fontTime = new Font("ＭＳ ゴシック", Font.BOLD, 24);

	// ゲームオーバー
	private Font fontGameOver = new Font("ＭＳ ゴシック", Font.BOLD, 48);

	/**
	 * コンストラクタ
	 */
	public MGPanel() {

		// スーパークラスを呼び出す
		super();

		try{

			// パネルサイズ
			super.setPreferredSize(new Dimension(800, 600));
			// レイアウト設定
			super.setLayout(null);

			// マウスアダプタを生成
			mgma = new MGMouseAdapter();
			// パネルにマウスリスナーを追加する
			super.addMouseListener(mgma);
			super.addMouseMotionListener(mgma);

			// 背景を読み込む
			InputStream isBackground = this.getClass().getResourceAsStream("Background.jpg");
			imageBackground = ImageIO.read(isBackground);
			isBackground.close();

			// ピコピコハンマーを読み込む
			imagePHs = new BufferedImage[2];
			InputStream isPH00 = this.getClass().getResourceAsStream("PH00.gif");
			imagePHs[0] = ImageIO.read(isPH00);
			isPH00.close();

			InputStream isPH01 = this.getClass().getResourceAsStream("PH01.gif");
			imagePHs[1] = ImageIO.read(isPH01);
			isPH01.close();

			// もぐらを読み込む
			imageMs = new BufferedImage[2];
			InputStream isM00 = this.getClass().getResourceAsStream("M00.gif");
			imageMs[0] = ImageIO.read(isM00);
			isM00.close();

			InputStream isM01 = this.getClass().getResourceAsStream("M01.gif");
			imageMs[1] = ImageIO.read(isM01);
			isM01.close();

			// 初期化
			init();

			// タイマーを生成
			timerThis = new java.util.Timer();

			// タイマーをスタート
			timerThis.scheduleAtFixedRate(new TimerActionTimerTask(), 1000l, 16l);

		}catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "ERROR : " + ex.toString());
		}

	}

	/**
	 * 初期化
	 */
	public void init() {

		// フェーズをゲーム中にする
		phase = 0;
		// もぐらの状態
		m = 0;
		// 得点
		score = 0;
		// 時間
		time = 1859;

	} // end init

	/**
	 * 実行
	 */
	public void run() {

		// フェーズ
		switch (phase) {

		case 0:

			// 制限時間を減らす
			time--;

			// 制限時間が来たら、
			if (time == 0) {
				// 時間を戻す
				time = 300;
				// フェーズ変更
				phase = 1;
				// ブレイク
				break;
			}

			// やられている場合、
			if (timeM != 0) {

				// 時間を－１する
				timeM--;

				// 時間が０になったら
				if (timeM == 0) {

					// 新しいもぐらを表示する
					m = 0;

					mx = (int) (Math.random() * 550);
					my = (int) (Math.random() * 450);

				} // end if 時間が０になったら

			} // end if やられている場合

			// ブレイク
			break;

		case 1:

			// 制限時間を減らす
			time--;

			// 制限時間が来たら、
			if (time == 0) {
				// 初期化
				init();
			}

			// ブレイク
			break;

		} // end switch

	} // end run

	/**
	 * 描画メソッド.
	 * <pre>
	 * ペイントする必要があるときにこのメソッドを呼び出されます。
	 * </pre>
	 */
	public void paint(Graphics g) {

		// 背景を描画する
		g.drawImage(imageBackground, 0, 0, 800, 600, this);

		// もぐらを描画する
		g.drawImage(imageMs[m], mx, my, 100, 100, this);

		// ピコピコハンマーを描画する
		g.drawImage(imagePHs[ph], px, py, 100, 88, this);

		// 得点を表示する
		g.setColor(Color.yellow);
		g.setFont(fontScore);
		g.drawString("得点：" + score, 0, 24);

		// ゲーム中の場合、
		if (phase == 0) {

			// 制限時間を表示する
			g.setColor(Color.red);
			g.setFont(fontTime);
			g.drawString("残り時間：" + time / 60, 300, 24);

			// ゲームオーバーの場合、
		} else if (phase == 1) {
			g.setColor(Color.lightGray);
			g.setFont(fontGameOver);
			g.drawString("ゲームオーバー", 240, 300);

			// コメント
			if (score >= 30) {
				g.drawString("おぬしやるやん", 240, 350);
			} else if (score >= 20) {
				g.drawString("もう一歩", 240, 350);
			} else if (score >= 10) {
				g.drawString("まだまだじゃ", 240, 350);
			} else {
				g.drawString("がんばれ", 240, 350);
			}

		} // end if ゲームオーバーの場合

	} // end paint

	/**
	 * マウスアダプタ
	 */
	private class MGMouseAdapter extends MouseAdapter {

		/**
		 * マウスが押された時に呼ばれます
		 */
		public void mousePressed(MouseEvent me) {

			// ゲーム中の場合、
			if (phase == 0) {

				// ピコピコハンマー叩く
				ph = 1;
				// 場所を記憶する
				px = me.getX() - 100;
				py = me.getY() - 100;

				// やられていない場合、
				if (m == 0) {

					// もぐらとの当たり判定
					if (px > mx - 50 && px < mx + 90 && py > my - 70 && py < my + 60) {
						// やられた
						m = 1;
						timeM = 30;
						score++;
					}

				} // end if やられていない場合

			} // end if ゲーム中の場合

		} //  end mousePressed

		/**
		 * マウスが離された時に呼ばれます
		 */
		public void mouseReleased(MouseEvent me) {

			// ピコピコハンマーを上げる
			ph = 0;
			// 場所を記憶する
			px = me.getX() - 100;
			py = me.getY() - 100;

		} //  end mouseReleased

		/**
		 * マウスが移動された時に呼ばれます
		 */
		public void mouseMoved(MouseEvent me) {

			// 場所を記憶する
			px = me.getX() - 100;
			py = me.getY() - 100;

		} //  end mouseMoved

		/**
		 * マウスがドラッグされた時に呼ばれます
		 */
		public void mouseDragged(MouseEvent me) {

			// 場所を記憶する
			px = me.getX() - 100;
			py = me.getY() - 100;

		} //  end mouseDragged

	} // end MGMouseAdapter

	/**
	 * タイマークラス.
	 */
	private class TimerActionTimerTask extends TimerTask {

		public void run() {

			// 実行メソッド呼び出し
			MGPanel.this.run();

			// 描画する
			repaint();

		} // end actionPerformed

	} // end TimerActionTimerTask

}
