ALTER TABLE public.student_test
    ADD COLUMN test_result integer,
    ADD COLUMN student_test_id bigserial PRIMARY KEY;