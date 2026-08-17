"use client";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import Link from "next/link";
import { apiFetch } from "@/lib/api";
import { BookPreviewResponse } from "@/lib/types";
import Header from "@/components/Header";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

export default function PreviewPage() {
  const params = useParams();
  const questionSetId = params.id as string;

  const [data, setData] = useState<BookPreviewResponse | null>(null);
  const [index, setIndex] = useState(0);
  const [loading, setLoading] = useState(true);
  const [shareLink, setShareLink] = useState<string | null>(null);
  const [sharing, setSharing] = useState(false);

  useEffect(() => {
    apiFetch(`/api/question-sets/${questionSetId}/preview`)
      .then(setData)
      .finally(() => setLoading(false));
  }, [questionSetId]);

  const handleShare = async () => {
    setSharing(true);
    try {
      const res = await apiFetch(`/api/question-sets/${questionSetId}/share-links`, { method: "POST" });
      setShareLink(`${window.location.origin}/share/${res.token}`);
    } finally {
      setSharing(false);
    }
  };

  if (loading) return <p className="px-8 py-16">불러오는 중...</p>;
  if (!data || data.entries.length === 0) {
    return (
      <>
        <Header />
        <main className="mx-auto max-w-2xl px-8 py-16 text-center">
          <p className="text-gray-500">아직 답변된 질문이 없어요.</p>
          <Link href={`/question-sets/${questionSetId}`} className="mt-4 inline-block text-sm underline">
            질문 목록으로 돌아가기
          </Link>
        </main>
      </>
    );
  }

  const entry = data.entries[index];

  return (
    <>
      <Header />

      <main className="mx-auto max-w-4xl px-8 py-16">
        <div className="grid grid-cols-1 overflow-hidden rounded-lg border border-gray-200 shadow-sm sm:grid-cols-2">
          <div className="flex items-center justify-center bg-gray-50 p-8">
            {entry.imageUrl ? (
              <img
                src={`${API_BASE_URL}${entry.imageUrl}`}
                alt="답변 사진"
                className="max-h-80 w-full rounded object-cover"
              />
            ) : (
              <div className="flex h-80 w-full items-center justify-center rounded bg-gray-100 text-sm text-gray-300">
                사진 없음
              </div>
            )}
          </div>
          <div className="flex flex-col justify-center bg-[#fbf8f3] p-8">
            <p className="font-serif text-sm text-gray-400">Q. {entry.questionText}</p>
            <p className="mt-4 font-serif text-lg italic leading-relaxed text-gray-700">
              &ldquo;{entry.content}&rdquo;
            </p>
          </div>
        </div>

        <div className="mt-6 flex items-center justify-between">
          <button
            onClick={() => setIndex((i) => Math.max(0, i - 1))}
            disabled={index === 0}
            className="text-sm text-gray-500 disabled:opacity-30"
          >
            ← 이전
          </button>
          <p className="text-sm text-gray-400">
            {index + 1} / {data.entries.length}
          </p>
          <button
            onClick={() => setIndex((i) => Math.min(data.entries.length - 1, i + 1))}
            disabled={index === data.entries.length - 1}
            className="text-sm text-gray-500 disabled:opacity-30"
          >
            다음 →
          </button>
        </div>

        <div className="mt-16 border-t border-gray-100 pt-8 text-center">
          {shareLink ? (
            <div>
              <p className="text-sm text-gray-500">이 링크로 공유할 수 있어요 (30일간 유효)</p>
              <p className="mt-2 break-all rounded-lg bg-gray-50 px-4 py-3 text-sm">{shareLink}</p>
            </div>
          ) : (
            <button
              onClick={handleShare}
              disabled={sharing}
              className="rounded-full bg-gray-900 px-6 py-3 text-sm text-white hover:bg-gray-700 disabled:opacity-50"
            >
              {sharing ? "링크 만드는 중..." : "공유 링크 만들기"}
            </button>
          )}
        </div>
      </main>
    </>
  );
}