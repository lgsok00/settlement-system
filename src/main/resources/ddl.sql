-- Video 데이터 삽입
INSERT INTO video (title, url, description, running_time, video_view_count, next_calc_date, created_at, updated_at)
VALUES
    ('Video 1', 'http://example.com/video1', 'Description for video 1', 700, 0, NULL, NOW(), NOW()),
    ('Video 2', 'http://example.com/video2', 'Description for video 2', 500, 0, NULL, NOW(), NOW()),
    ('Video 3', 'http://example.com/video3', 'Description for video 3', 400, 0, NULL, NOW(), NOW()),
    ('Video 4', 'http://example.com/video4', 'Description for video 3', 300, 0, NULL, NOW(), NOW());

-- Ad 데이터 삽입
INSERT INTO ad (title, url, description, created_at, updated_at)
VALUES
    ('Ad 1', 'http://example.com/ad1', 'Description for ad 1', NOW(), NOW()),
    ('Ad 2', 'http://example.com/ad2', 'Description for ad 2', NOW(), NOW()),
    ('Ad 3', 'http://example.com/ad3', 'Description for ad 3', NOW(), NOW()),
    ('Ad 4', 'http://example.com/ad4', 'Description for ad 4', NOW(), NOW()),
    ('Ad 5', 'http://example.com/ad5', 'Description for ad 5', NOW(), NOW());

-- VideoAdList 데이터 삽입
INSERT INTO video_ad_list (video_id, ad_id, video_ad_view_count, created_at, updated_at)
VALUES
    (1, 1, 0, NOW(), NOW()),
    (1, 2, 0, NOW(), NOW()),
    (2, 3, 0, NOW(), NOW()),
    (2, 4, 0, NOW(), NOW()),
    (3, 5, 0, NOW(), NOW()),
    (4, 5, 0, NOW(), NOW())
;
