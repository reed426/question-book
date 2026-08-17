"use client";
import { useEffect, useState } from "react";
import Link from "next/link";
import Header from "@/components/Header";
import { apiFetch } from "@/lib/api";
import { NotificationItem } from "@/lib/types";

export default function NotificationsPage() {
  const [items, setItems] = useState<NotificationItem[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiFetch("/api/notifications")
      .then(setItems)
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <Header />
      <main className="mx-auto max-w-2xl px-8 py-16">
        <h1 className="text-2xl font-bold">지금 답할 수 있는 질문</h1>
        <p className="mt-2 text-sm text-gray-500">잠금이 풀렸지만 아직 답하지 않은 질문들이에요.</p>

        {loading ? (
          <p className="mt-12 text-sm text-gray-400">불러오는 중...</p>
        ) : items.length === 0 ? (
          <p className="mt-12 text-sm text-gray-500">지금은 답할 새 질문이 없어요.</p>
        ) : (
          <ul className="mt-8 space-y-3">
            {items.map((item) => (
              <li key={item.questionId} className="rounded-lg border border-gray-200 p-4">
                <p className="text-xs text-rose-500">{item.questionSetTitle}</p>
                <Link
                  href={`/question-sets/${item.questionSetId}/questions/${item.questionId}/answer`}
                  className="mt-1 block text-sm hover:underline"
                >
                  {item.questionText}
                </Link>
              </li>
            ))}
          </ul>
        )}
      </main>
    </>
  );
}