"use client";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { apiFetch } from "@/lib/api";
import { BookPreviewResponse } from "@/lib/types";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080";

export default function SharedBookPage() {
  const params = useParams();
  const token = params.token as string;
  const [data, setData] = useState<BookPreviewResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    apiFetch(`/api/share/${token}`)
      .then(setData)
      .catch(() => setError(true))
      .finally(() => setLoading(false));
  }, [token]);

  if (loading) return <p className="px-8 py-16 text-center">불러오는 중...</p>;
  if (error || !data) {
    return <p className="px-8 py-16 text-center text-gray-500">유효하지 않거나 해제된 링크예요.</p>;
  }

  return (
    <main className="mx-auto max-w-3xl px-8 py-16">
      <h1 className="text-center text-2xl font-bold">{data.authorNickname}의 기록</h1>
      <div className="mt-10 space-y-10">
        {data.entries.map((entry) => (
          <div key={entry.questionId} className="rounded-lg border border-gray-200 bg-[#fbf8f3] p-8">
            <p className="font-serif text-sm text-gray-400">Q. {entry.questionText}</p>
            <p className="mt-4 font-serif text-lg italic leading-relaxed text-gray-700">
              &ldquo;{entry.content}&rdquo;
            </p>
            {entry.imageUrl && (
              <img
                src={`${API_BASE_URL}${entry.imageUrl}`}
                alt="답변 사진"
                className="mt-4 max-h-80 rounded object-cover"
              />
            )}
          </div>
        ))}
      </div>
    </main>
  );
}