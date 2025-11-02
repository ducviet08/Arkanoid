Arkanoid

**Thành viên:**

-Lê Đức Việt: 24020360 25%

-Nguyễn Đắc Vượng: 24020369 25%

-Nguyễn Duy Tú: 24020342 25%

-Nguyễn Văn Toán: 24020324 25%

**Mô tả về các đối tượng:**

+Paddle(thanh đỡ): là đối tượng mà người chơi có thể điều khiển sang trái hoặc phải để đỡ bóng.

+Ball(bóng): là đối tượng mà người chơi phải cố gắng đỡ lấy không cho rớt xuống đáy và cố gắng điều đối tượng này đề phá vỡ hết các đối tượng gạch ở trên màn chơi 

+Brick(gạch): là đối tượng mà người chơi cố gắng phá hủy để hoanf thành màn chơi

    -NormalBrick: là loại gạch có 1 máu
    
    -StrongBrick: là loại gạch có 2 máu
    
    -GlassBrick: là loại gạch có 2 máu, khi nó đầy máu thì nó sẽ tàng hình
    
    -ExplosiveBrick: là loại gạch có 1 máu, khi bị phá húy sẽ tạo hệu ứng nổ gây 1 sát thương lên các gạch xung quanh
    
+Steel(thép): là đối tượng không thể phá vỡ đi được

+PowerUp(vật phẩm): là đối tượng ngâ nhiên có thể rơi ra khi gạch bị phá hủy và ngầu nhiên nhận được các tính năng bất kì khi thanh đỡ ăn được chúng

    -ExpandPaddlePowerUp: tăng chiều dài thanh đỡ
    
    -ExtraLifePowerUp: tăng cho người chơi 1 máu(tối đa 5 máu)
    
    -FastBallPowerUp: tăng tốc độ bóng
    
    -FireballPowerUp: khiến bóng trở nên hủy diệt, phá hủy tất cả gạch khi bóng đi qua
    
    -ShrinkPaddlePowerUp: giảm chiều dài thanh đỡ
    
    -StickyPaddlePowerUp: làm cho thanh đỡ trở nên dính 
    
    -TinyBall: thu nhỏ kích thước bóng

**Mô tả gameplay, xử lí va chạm**

+Trong màn chơi, Paddle sẽ được người chơi di chuyển để đỡ Ball và điều huướng Ball với mục tiêu chính là phá vỡ hết Brick

+Khi phá vỡ hết Brick, người chơi sẽ qua màn mới

+Mỗi lần Ball bị rớt xuống hết màn hình, mạng của người chơi bị trừ

+Bao giờ mạng của người chơi hết, trò chơi kết thúc

+Khi va chạm Ball với Brick thì Brick sẽ mất máu

+Brick bị phá hủy khi máu còn 0

+Khi va cham với các vật thể cố định hoặc biên, Ball sẽ bị đổi hướng bật lại

+Khi va chạm với Paddle, Ball sẽ bị đổi hướng theo vị tr va phải Paddle và thay đổi tốc độ tùy theo tính hình va chạm


**Mô tả chế độ chơi**

+1player: Người chơi sẽ chơi theo màn, phá hủy hết brick của màn này để đến level màn tiếp theo

+2player: 2 người chơi sẽ chơi ở 1 map bất kì, người cho này sẽ chiến thắng khi phá vỡ hết gạch trước người kia hoặc người kia mất hết mạng

+Có chế độ Continue ở 1player để chơi tiếp tục khi thoát game vào lại

+Có bảng hiển thị highscore để xem tên các người chơi cao điểm và số điểm họ đạt được


**Các phân việc của mỗi thành viên**

-Lê Đức Việt: Quản lí GameLoop, GameManager,2player(*)

-Nguyễn Đắc Vượng: Brick,Map,Steel,SaveLoadGame(*)

-Nguyễn Duy Tú:View,Images,PowerUps,LoadLevel,HighScore

-Nguyễn Văn Toán: Ball,Paddle,StickyPaddle(*)

(*)bounceOff(Tú+Toán)


