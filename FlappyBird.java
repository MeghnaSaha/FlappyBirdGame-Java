package FlappyBirdGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
	
	public static FlappyBird flappyBird;
	public final int WIDTH = 1000, HEIGHT = 700;
	public Renderer renderer;
	public Random rand;
	public Rectangle bird;
	public int birdSize = 20;
	public ArrayList<Rectangle> columns;
	public boolean started, gameOver;
	public int ticks, yMotion, score;
	
	FlappyBird() {
		
		JFrame jframe = new JFrame();
		Timer timer = new Timer(25, this); // actionPerformed method is called every 20 ms
		renderer = new Renderer();
		rand = new Random();
		
		jframe.add(renderer);
		jframe.setTitle("Krappy Bird");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);
		jframe.setVisible(true);
		jframe.setResizable(false);
		jframe.setLocationRelativeTo(null);
		
		bird = new Rectangle(WIDTH/2, HEIGHT/2 - 50, birdSize, birdSize);
		columns = new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
	}

	public static void main(String[] args) {
		flappyBird = new FlappyBird();
	}
	
	public void addColumn(boolean start) {
		
		int vspace = 200; //vertical space between upper and lower columns
		int hspace = 400; //horizontal space 2 columns
		int width = 100;
		int height = 50 + rand.nextInt(200); //for lower column
		
		if(start) {
			columns.add(new Rectangle(WIDTH + columns.size() *(width+hspace)/2, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(WIDTH + (columns.size()-1) *(width+hspace)/2, 0 , width, HEIGHT - height - vspace - 120));
		}
		else {
			columns.add(new Rectangle(columns.get(columns.size()-1).x + hspace + width, HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size()-1).x , 0 , width, HEIGHT - height - vspace - 120));
		}
		
		
	}

	public void repaint(Graphics g) {
		
		// Background
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.ORANGE);	
		g.fillRect(0, HEIGHT-120, WIDTH, 120);
		g.setColor(Color.GREEN);
		g.fillRect(0, HEIGHT-120, WIDTH, 15);
		
		// Bird
		g.setColor(Color.RED);
		g.fillRect(bird.x, bird.y, bird.width, bird.height);
		
		// Obstacles
		for(Rectangle column : columns) {
			paintColumn(g, column);
		}
		
		// Texts
		g.setColor(Color.WHITE);
		if(!started) {
			g.setFont(new Font("Arial", 1, 60));
			g.drawString("Click to Start!", 300, 250);
		}
		if(gameOver) {
			g.setFont(new Font("Arial", 1, 90));
			g.drawString("Game Over!", 250, 300);
		}
		if(started) {
			g.setFont(new Font("Arial", 4, 20));
			g.drawString("Score : " + String.valueOf(score), WIDTH/2 - 50, 30);
		}
		
	}
	
	public void paintColumn(Graphics g, Rectangle column) {
		
		g.setColor(Color.GREEN.darker().darker());
		g.fillRect(column.x, column.y, column.width, column.height);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		ticks++;
		
		if(started) {
			int speed = 10;
			for(Rectangle column : columns) {
				column.x -= speed;
			}
			for(int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				if(column.x + column.width <= 0) {
					columns.remove(column);
					if(column.y == 0) {
						addColumn(false);
					}
				}
			}
			if(ticks % 2 == 0 && yMotion < 15) {
				yMotion += 1; //for when bird is not poked to fly (kinda like gravity)
			}
			bird.y += yMotion;
			for(Rectangle column : columns) {
				if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) //exact center of column since bird has size 20
				{
					if(column.y == 0) //so that score adds once for a set of upper and lower columns
					{
						score++;
					}
				}
				if(column.intersects(bird)) {
					gameOver = true;
					bird.x = column.x - bird.width;
				}
			}
			if(bird.y + bird.height >= HEIGHT - 120 || bird.y <= 0) {
				gameOver = true;
			}
			if(gameOver) {
				bird.y = HEIGHT - 120 - bird.height;
			}
		}
		
		renderer.repaint();
		
	}
	
	private void tryToFly() {
		
		if(gameOver) {
			bird = new Rectangle(WIDTH/2, HEIGHT/2 - 50, birdSize, birdSize);
			columns.clear();
			score = 0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver = false;
		}
		
		if(!started) {
			started = true;
		}
		else if(!gameOver) {
			if(yMotion > 0) {
				yMotion = 0;
			}
			yMotion -= 10;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		tryToFly();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				tryToFly();	
		}
	}

}
