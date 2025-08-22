-- 1) 상점(명시적 ID 사용)
INSERT INTO stores (id, name, category, latitude, longitude) VALUES
  (1, 'Good Burger', 'burger', 37.5665, 126.9780),
  (2, 'Seoul Sushi', 'sushi', 37.5650, 126.9895),
  (3, 'Taco Fiesta', 'mexican', 37.5700, 126.9820),
  (4, 'K-Cafe', 'cafe', 37.5610, 126.9830),
  (5, 'Pasta House', 'italian', 37.5680, 126.9760),
  (6, 'BBQ Heaven', 'korean', 37.5640, 126.9750),
  (7, 'Veggie Place', 'vegan', 37.5630, 126.9900),
  (8, 'Boulangerie', 'bakery', 37.5670, 126.9860);

-- ⚠️ IDENTITY(자동증가) 값 재시작(다음 insert는 9부터)
ALTER TABLE stores ALTER COLUMN id RESTART WITH 9;

-- 2) 상품 (최저가 비교용으로 같은 base_product_id를 상점별로 다르게)
--    테이블명/컬럼명은 엔티티 @Table/@Column 네이밍 규칙에 맞춰 스네이크케이스 사용
--    Product 엔티티: @Table(name="products")
--    baseProductId -> base_product_id, store -> store_id
INSERT INTO products (base_product_id, name, price, store_id, stock, discount_rate) VALUES
  -- 우유 1L (base 101): 상점 1은 1900원, 상점 2는 1800원
  (101, '우유 1L', 1900, 1, 999, 0),
  (101, '우유 1L', 1800, 2, 999, 0),

  -- 식빵 (base 202): 상점 1이 2500원, 상점 2가 2600원
  (202, '식빵',    2500, 1, 999, 10),  -- 10% 할인 예시
  (202, '식빵',    2600, 2, 999, 0),

  -- 계란 10개 (base 303)
  (303, '계란 10개', 4980, 5, 500, 0),
  (303, '계란 10개', 5200, 6, 500, 0);

-- (선택) products의 IDENTITY도 필요하면 RESTART
-- ALTER TABLE products ALTER COLUMN id RESTART WITH 1000;

