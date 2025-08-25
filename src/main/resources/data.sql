INSERT INTO stores (id, name, category, latitude, longitude, address, phone_number, open_time, close_time) VALUES
  (1, '신선마트', '마트', 37.5665, 126.9780, '서울시 중구 명동길 123', '02-1234-5678', '08:00:00', '22:00:00'),
  (2, '한우정육점', '정육점', 37.5650, 126.9895, '서울시 중구 명동길 456', '02-2345-6789', '09:00:00', '21:00:00'),
  (3, '청과마을', '채소가게', 37.5700, 126.9820, '서울시 중구 명동길 789', '02-3456-7890', '07:00:00', '20:00:00'),
  (4, '바다횟집', '수산물', 37.5610, 126.9830, '서울시 중구 명동길 012', '02-4567-8901', '10:00:00', '23:00:00'),
  (5, '동네슈퍼', '편의점', 37.5680, 126.9760, '서울시 중구 명동길 345', '02-5678-9012', '06:00:00', '23:59:59'),
  (6, '김치마을', '반찬가게', 37.5640, 126.9750, '서울시 중구 명동길 678', '02-6789-0123', '08:00:00', '19:00:00'),
  (7, '건강식품점', '건강식품', 37.5630, 126.9900, '서울시 중구 명동길 901', '02-7890-1234', '09:00:00', '20:00:00'),
  (8, '떡방앗간', '떡집', 37.5670, 126.9860, '서울시 중구 명동길 234', '02-8901-2345', '07:00:00', '18:00:00');

-- 상품 데이터 추가 (모든 이미지 경로 수정 완료)
INSERT INTO products (id, name, description, price, stock, unit, discount_rate, discount_price, image_url, store_id) VALUES
  -- 신선마트 (1번)
  (1, '삼겹살', '국산 삼겹살 500g', 12000, 50, '500g', 10, NULL, 'https://meokgeori.vercel.app/images/01_PorkBelly.jpg', 1),
  (2, '상추', '신선한 상추 한 묶음', 3000, 30, '묶음', 0, NULL, 'https://meokgeori.vercel.app/images/02_SSAM.jpg', 1),
  (3, '마늘', '국산 마늘 500g', 4000, 40, '500g', 5, NULL, 'https://meokgeori.vercel.app/images/03_Garlic.jpg', 1),
  (4, '양파', '국산 양파 1kg', 2500, 60, '1kg', 0, NULL, 'https://meokgeori.vercel.app/images/04_Onion.jpg', 1),
  (5, '계란', '신선한 계란 30개들이', 8000, 25, '30개', 0, NULL, 'https://meokgeori.vercel.app/images/05_Egg.jpg', 1),

  -- 한우정육점 (2번)
  (6, '한우등심', '1++ 등급 한우등심 300g', 35000, 15, '300g', 15, NULL, 'https://meokgeori.vercel.app/images/06_KoreanBeefSirloin.jpg', 2),
  (7, '돼지갈비', '국산 돼지갈비 1kg', 18000, 20, '1kg', 10, NULL, 'https://meokgeori.vercel.app/images/07_PorkRib.jpg', 2),
  (8, '닭가슴살', '국산 닭가슴살 500g', 6000, 35, '500g', 0, NULL, 'https://meokgeori.vercel.app/images/08_ChickenBreast.jpg', 2),
  (9, '소고기국거리', '국산 소고기 국거리용 500g', 15000, 25, '500g', 5, NULL, 'https://meokgeori.vercel.app/images/09_BeefForSoup.jpg', 2),

  -- 청과마을 (3번)
  (10, '시금치', '유기농 시금치 200g', 2000, 40, '200g', 0, NULL, 'https://meokgeori.vercel.app/images/10_Spinach.jpg', 3),
  (11, '당근', '국산 당근 500g', 2500, 50, '500g', 0, NULL, 'https://meokgeori.vercel.app/images/11_Carrot.jpg', 3),
  (12, '오이', '국산 오이 3개', 3000, 45, '3개', 0, NULL, 'https://meokgeori.vercel.app/images/12_Cucumber.jpg', 3),
  (13, '토마토', '방울토마토 500g', 4500, 35, '500g', 10, NULL, 'https://meokgeori.vercel.app/images/13_Tomato.jpg', 3),
  (14, '배추', '국산 배추 1포기', 3500, 20, '1포기', 0, NULL, 'https://meokgeori.vercel.app/images/14_NapaCabbage.jpg', 3),

  -- 바다횟집 (4번)
  (15, '고등어', '신선한 고등어 1마리', 8000, 15, '1마리', 0, NULL, 'https://meokgeori.vercel.app/images/15_Mackerel.jpg', 4),
  (16, '연어', '노르웨이산 연어 300g', 15000, 10, '300g', 20, NULL, 'https://meokgeori.vercel.app/images/16_Salmon.jpg', 4),
  (17, '새우', '흰다리새우 500g', 12000, 20, '500g', 0, NULL, 'https://meokgeori.vercel.app/images/17_Shrimp.jpg', 4),
  (18, '오징어', '신선한 오징어 2마리', 10000, 18, '2마리', 5, NULL, 'https://meokgeori.vercel.app/images/18_Squid.jpg', 4),

  -- 동네슈퍼 (5번)
  (19, '쌀', '백미 10kg', 35000, 30, '10kg', 5, NULL, 'https://meokgeori.vercel.app/images/19_Rice.jpg', 5),
  (20, '라면', '신라면 5개입', 4500, 80, '5개입', 0, NULL, 'https://meokgeori.vercel.app/images/20_Shin5.jpg', 5),
  (21, '참기름', '참기름 500ml', 12000, 25, '500ml', 0, NULL, 'https://meokgeori.vercel.app/images/21_SesameOil.jpg', 5),
  (22, '간장', '진간장 1L', 8000, 40, '1L', 0, NULL, 'https://meokgeori.vercel.app/images/22_SoySauce.jpg', 5),
  (23, '설탕', '백설탕 1kg', 3000, 50, '1kg', 0, NULL, 'https://meokgeori.vercel.app/images/23_Sugar.jpg', 5),

  -- 김치마을 (6번)
  (24, '김치', '배추김치 1kg', 8000, 25, '1kg', 0, NULL, 'https://meokgeori.vercel.app/images/24_CabbageKimchi.jpg', 6),
  (25, '깍두기', '깍두기 500g', 5000, 20, '500g', 0, NULL, 'https://meokgeori.vercel.app/images/25_KkagDduGi.jpg', 6),
  (26, '젓갈', '새우젓 300g', 6000, 15, '300g', 0, NULL, 'https://meokgeori.vercel.app/images/26_ShrimpPaste.jpg', 6),
  (27, '나물', '시래기나물 200g', 4000, 30, '200g', 0, NULL, 'https://meokgeori.vercel.app/images/27_Shiraegi.jpg', 6),

  -- 건강식품점 (7번)
  (28, '견과류', '아몬드 500g', 15000, 20, '500g', 10, NULL, 'https://meokgeori.vercel.app/images/28_Almond.jpg', 7),
  (29, '올리브오일', '엑스트라버진 올리브오일 500ml', 18000, 15, '500ml', 0, NULL, 'https://meokgeori.vercel.app/images/29_OliveOil.jpg', 7),
  (30, '홍삼', '6년근 홍삼 100g', 45000, 10, '100g', 5, NULL, 'https://meokgeori.vercel.app/images/30_RedGinseng.jpg', 7),

  -- 떡방앗간 (8번)
  (31, '가래떡', '가래떡 500g', 5000, 30, '500g', 0, NULL, 'https://meokgeori.vercel.app/images/31_GaraeTteok.jpg', 8),
  (32, '떡국떡', '떡국떡 1kg', 8000, 25, '1kg', 0, NULL, 'https://meokgeori.vercel.app/images/32_DGD.jpg', 8),
  (33, '인절미', '인절미 10개', 7000, 20, '10개', 0, NULL, 'https://meokgeori.vercel.app/images/33_Injeolmi.jpg', 8);