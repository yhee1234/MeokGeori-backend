-- 기존 상점 데이터 (카테고리를 한국식으로 변경)
INSERT INTO stores (id, name, category, latitude, longitude, address, phone_number, open_time, close_time) VALUES
  (1, '신선마트', '마트', 37.5665, 126.9780, '서울시 중구 명동길 123', '02-1234-5678', '08:00:00', '22:00:00'),
  (2, '한우정육점', '정육점', 37.5650, 126.9895, '서울시 중구 명동길 456', '02-2345-6789', '09:00:00', '21:00:00'),
  (3, '청과마을', '채소가게', 37.5700, 126.9820, '서울시 중구 명동길 789', '02-3456-7890', '07:00:00', '20:00:00'),
  (4, '바다횟집', '수산물', 37.5610, 126.9830, '서울시 중구 명동길 012', '02-4567-8901', '10:00:00', '23:00:00'),
  (5, '동네슈퍼', '편의점', 37.5680, 126.9760, '서울시 중구 명동길 345', '02-5678-9012', '06:00:00', '23:59:59'),
  (6, '김치마을', '반찬가게', 37.5640, 126.9750, '서울시 중구 명동길 678', '02-6789-0123', '08:00:00', '19:00:00'),
  (7, '건강식품점', '건강식품', 37.5630, 126.9900, '서울시 중구 명동길 901', '02-7890-1234', '09:00:00', '20:00:00'),
  (8, '떡방앗간', '떡집', 37.5670, 126.9860, '서울시 중구 명동길 234', '02-8901-2345', '07:00:00', '18:00:00');

-- 상품 데이터 추가
INSERT INTO products (id, name, description, price, stock, unit, discount_rate, discount_price, image_url, store_id) VALUES
  -- 신선마트 (1번)
  (1, '삼겹살', '국산 삼겹살 500g', 12000, 50, '500g', 10, NULL, 'https://example.com/images/pork_belly.jpg', 1),
  (2, '상추', '신선한 상추 한 묶음', 3000, 30, '묶음', 0, NULL, 'https://example.com/images/lettuce.jpg', 1),
  (3, '마늘', '국산 마늘 500g', 4000, 40, '500g', 5, NULL, 'https://example.com/images/garlic.jpg', 1),
  (4, '양파', '국산 양파 1kg', 2500, 60, '1kg', 0, NULL, 'https://example.com/images/onion.jpg', 1),
  (5, '계란', '신선한 계란 30개들이', 8000, 25, '30개', 0, NULL, 'https://example.com/images/eggs.jpg', 1),
  
  -- 한우정육점 (2번)
  (6, '한우등심', '1++ 등급 한우등심 300g', 35000, 15, '300g', 15, NULL, 'https://example.com/images/hanwoo_sirloin.jpg', 2),
  (7, '돼지갈비', '국산 돼지갈비 1kg', 18000, 20, '1kg', 10, NULL, 'https://example.com/images/pork_ribs.jpg', 2),
  (8, '닭가슴살', '국산 닭가슴살 500g', 6000, 35, '500g', 0, NULL, 'https://example.com/images/chicken_breast.jpg', 2),
  (9, '소고기국거리', '국산 소고기 국거리용 500g', 15000, 25, '500g', 5, NULL, 'https://example.com/images/beef_soup.jpg', 2),
  
  -- 청과마을 (3번)
  (10, '시금치', '유기농 시금치 200g', 2000, 40, '200g', 0, NULL, 'https://example.com/images/spinach.jpg', 3),
  (11, '당근', '국산 당근 500g', 2500, 50, '500g', 0, NULL, 'https://example.com/images/carrot.jpg', 3),
  (12, '오이', '국산 오이 3개', 3000, 45, '3개', 0, NULL, 'https://example.com/images/cucumber.jpg', 3),
  (13, '토마토', '방울토마토 500g', 4500, 35, '500g', 10, NULL, 'https://example.com/images/cherry_tomato.jpg', 3),
  (14, '배추', '국산 배추 1포기', 3500, 20, '1포기', 0, NULL, 'https://example.com/images/cabbage.jpg', 3),
  
  -- 바다횟집 (4번)
  (15, '고등어', '신선한 고등어 1마리', 8000, 15, '1마리', 0, NULL, 'https://example.com/images/mackerel.jpg', 4),
  (16, '연어', '노르웨이산 연어 300g', 15000, 10, '300g', 20, NULL, 'https://example.com/images/salmon.jpg', 4),
  (17, '새우', '흰다리새우 500g', 12000, 20, '500g', 0, NULL, 'https://example.com/images/shrimp.jpg', 4),
  (18, '오징어', '신선한 오징어 2마리', 10000, 18, '2마리', 5, NULL, 'https://example.com/images/squid.jpg', 4),
  
  -- 동네슈퍼 (5번)
  (19, '쌀', '백미 10kg', 35000, 30, '10kg', 5, NULL, 'https://example.com/images/rice.jpg', 5),
  (20, '라면', '신라면 5개입', 4500, 80, '5개입', 0, NULL, 'https://example.com/images/ramen.jpg', 5),
  (21, '참기름', '참기름 500ml', 12000, 25, '500ml', 0, NULL, 'https://example.com/images/sesame_oil.jpg', 5),
  (22, '간장', '진간장 1L', 8000, 40, '1L', 0, NULL, 'https://example.com/images/soy_sauce.jpg', 5),
  (23, '설탕', '백설탕 1kg', 3000, 50, '1kg', 0, NULL, 'https://example.com/images/sugar.jpg', 5),
  
  -- 김치마을 (6번)
  (24, '김치', '배추김치 1kg', 8000, 25, '1kg', 0, NULL, 'https://example.com/images/kimchi.jpg', 6),
  (25, '깍두기', '깍두기 500g', 5000, 20, '500g', 0, NULL, 'https://example.com/images/kkakdugi.jpg', 6),
  (26, '젓갈', '새우젓 300g', 6000, 15, '300g', 0, NULL, 'https://example.com/images/shrimp_paste.jpg', 6),
  (27, '나물', '시래기나물 200g', 4000, 30, '200g', 0, NULL, 'https://example.com/images/dried_radish.jpg', 6),
  
  -- 건강식품점 (7번)
  (28, '견과류', '아몬드 500g', 15000, 20, '500g', 10, NULL, 'https://example.com/images/almonds.jpg', 7),
  (29, '올리브오일', '엑스트라버진 올리브오일 500ml', 18000, 15, '500ml', 0, NULL, 'https://example.com/images/olive_oil.jpg', 7),
  (30, '홍삼', '6년근 홍삼 100g', 45000, 10, '100g', 5, NULL, 'https://example.com/images/red_ginseng.jpg', 7),
  
  -- 떡방앗간 (8번)
  (31, '가래떡', '가래떡 500g', 5000, 30, '500g', 0, NULL, 'https://example.com/images/rice_cake.jpg', 8),
  (32, '떡국떡', '떡국떡 1kg', 8000, 25, '1kg', 0, NULL, 'https://example.com/images/soup_rice_cake.jpg', 8),
  (33, '인절미', '인절미 10개', 7000, 20, '10개', 0, NULL, 'https://example.com/images/injeolmi.jpg', 8);
