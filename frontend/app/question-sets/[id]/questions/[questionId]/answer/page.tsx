"use client";
import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import Link from "next/link";
import { apiFetch } from "@/lib/api";
import Header from "@/components/Header";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

export default function AnswerPage() {
  const params = useParams();
  const router = useRouter();
  const questionSetId = params.id as string;
  const questionId = params.questionId as string;

  const [questionText, setQuestionText] = useState("");
  const [content, setContent] = useState("");
  const [existingImageUrl, setExistingImageUrl] = useState<string | null>(null);
  const [file, setFile] = useState<File | null>(null);
  const [fileObjectUrl, setFileObjectUrl] = useState<string | null>(null);
  const [previewOpen, setPreviewOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([
      apiFetch(`/api/question-sets/${questionSetId}`),
      apiFetch(`/api/questions/${questionId}/answer`),
    ])
      .then(([set, answer]) => {
        const q = set.questions.find((q: { id: number }) => q.id === Number(questionId));
        if (q) setQuestionText(q.text);
        if (answer) {
          setContent(answer.content ?? "");
          setExistingImageUrl(answer.imageUrl ?? null);
        }
      })
      .finally(() => setLoading(false));
  }, [questionSetId, questionId]);

  useEffect(() => {
    if (!file) {
      setFileObjectUrl(null);
      return;
    }
    const url = URL.createObjectURL(file);
    setFileObjectUrl(url);
    return () => URL.revokeObjectURL(url);
  }, [file]);

  const previewSrc = fileObjectUrl ?? (existingImageUrl ? `${API_BASE_URL}${existingImageUrl}` : null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setSubmitting(true);
    try {
      const formData = new FormData();
      formData.append("content", content);
      if (file) formData.append("image", file);
      await apiFetch(`/api/questions/${questionId}/answer`, {
        method: "POST",
        body: formData,
      });
      router.push(`/question-sets/${questionSetId}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : "저장에 실패했습니다");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <p className="px-8 py-16">불러오는 중...</p>;

  return (
    <>
      <Header />
      <main className="mx-auto max-w-3xl px-8 py-16">
        <p className="text-sm font-medium text-rose-500">질문</p>
        <h1 className="mt-2 text-xl font-bold">{questionText}</h1>

        <form onSubmit={handleSubmit} className="mt-8">
          <div className="flex flex-col gap-4 sm:flex-row">
            <textarea
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="이 질문에 대한 답을 적어주세요"
              required
              className="h-72 w-full flex-1 resize-none overflow-y-auto rounded-lg border border-gray-200 px-4 py-3 text-sm outline-none focus:border-gray-900"
            />

            <div className="shrink-0">
              {previewSrc ? (
                <>
                  <button
                    type="button"
                    onClick={() => setPreviewOpen(true)}
                    className="block h-32 w-32 overflow-hidden rounded-lg border border-gray-200"
                  >
                    <img src={previewSrc} alt="첨부 사진" className="h-full w-full object-cover" />
                  </button>
                  <label className="mt-2 block cursor-pointer text-center text-xs text-gray-400 underline">
                    사진 바꾸기
                    <input
                      type="file"
                      accept="image/*"
                      onChange={(e) => setFile(e.target.files?.[0] ?? null)}
                      className="hidden"
                    />
                  </label>
                </>
              ) : (
                <label className="flex h-32 w-32 cursor-pointer flex-col items-center justify-center gap-1 rounded-lg border border-dashed border-gray-300 text-gray-400 hover:border-gray-900 hover:text-gray-900">
                  <span className="text-2xl leading-none">+</span>
                  <span className="text-xs">사진 추가</span>
                  <input
                    type="file"
                    accept="image/*"
                    onChange={(e) => setFile(e.target.files?.[0] ?? null)}
                    className="hidden"
                  />
                </label>
              )}
            </div>
          </div>

          {error && <p className="mt-4 text-sm text-red-500">{error}</p>}

          <div className="mt-8 flex items-center gap-3">
            <button
              type="submit"
              disabled={submitting}
              className="rounded-full bg-gray-900 px-6 py-3 text-sm text-white hover:bg-gray-700 disabled:opacity-50"
            >
              {submitting ? "저장하는 중..." : "저장하기"}
            </button>
            <Link
              href={`/question-sets/${questionSetId}`}
              className="rounded-full px-6 py-3 text-sm text-gray-500 hover:text-gray-900"
            >
              취소
            </Link>
          </div>
        </form>

        {previewOpen && previewSrc && (
          <div
            className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-8"
            onClick={() => setPreviewOpen(false)}
          >
            <img
              src={previewSrc}
              alt="사진 크게 보기"
              className="max-h-[80vh] max-w-full rounded-lg object-contain"
              onClick={(e) => e.stopPropagation()}
            />
          </div>
        )}
      </main>
    </>
  );
}