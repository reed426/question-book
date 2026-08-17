"use client";
import Link from "next/link";
import { useAuth } from "@/context/AuthContext";

export default function Header() {
  const { user, logout } = useAuth();

  return (
    <header className="flex items-center justify-between border-b border-gray-100 px-8 py-5">
      <Link href="/" className="text-lg font-semibold tracking-tight">
        question-book
      </Link>
      <nav className="flex items-center gap-4 text-sm">
        {user ? (
          <>
          <Link href="/my" className="text-gray-600 hover:text-gray-900">
            내 질문북
          </Link>
          <Link href="/notifications" className="text-gray-600 hover:text-gray-900">
            알림
          </Link>
            <span className="text-gray-600 font-semibold">{user.nickname}님</span>
            <button onClick={logout} className="text-gray-500 hover:text-gray-900">
              로그아웃
            </button>
          </>
        ) : (
          <>
            <Link href="/login" className="text-gray-600 hover:text-gray-900">
              로그인
            </Link>
            <Link href="/signup" className="rounded-full bg-gray-900 px-4 py-2 text-white hover:bg-gray-700">
              시작하기
            </Link>
          </>
        )}
      </nav>
    </header>
  );
}