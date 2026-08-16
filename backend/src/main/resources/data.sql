INSERT INTO question_pack_template (id, name, target_type) VALUES (1, '연인에게', 'PARTNER');
INSERT INTO question_pack_template (id, name, target_type) VALUES (2, '가족에게', 'FAMILY');
INSERT INTO question_pack_template (id, name, target_type) VALUES (3, '나에게', 'SELF');

INSERT INTO template_question (id, template_id, sort_order, text) VALUES
                                                                      (1, 1, 1, '우리가 처음 만난 날, 가장 기억에 남는 순간은?'),
                                                                      (2, 1, 2, '나에게 가장 고마웠던 순간은 언제였어?'),
                                                                      (3, 1, 3, '함께 가보고 싶은 곳이 있다면?'),
                                                                      (4, 1, 4, '10년 뒤 우리는 어떤 모습일까?'),
                                                                      (5, 1, 5, '요즘 나한테 가장 하고 싶은 말은?'),
                                                                      (6, 2, 1, '어린 시절 가장 행복했던 기억은?'),
                                                                      (7, 2, 2, '나에게 해주고 싶었던 말이 있다면?'),
                                                                      (8, 2, 3, '살면서 가장 자랑스러웠던 순간은?'),
                                                                      (9, 2, 4, '나에게 남기고 싶은 이야기가 있다면?'),
                                                                      (10, 2, 5, '가장 후회되는 일이 있다면?'),
                                                                      (11, 3, 1, '요즘 나를 가장 힘들게 하는 건 뭐야?'),
                                                                      (12, 3, 2, '최근에 가장 행복했던 순간은?'),
                                                                      (13, 3, 3, '1년 뒤 나는 어떤 모습이었으면 좋겠어?'),
                                                                      (14, 3, 4, '지금 나에게 가장 필요한 건 뭘까?'),
                                                                      (15, 3, 5, '오늘 하루 나에게 칭찬 한마디 해준다면?');