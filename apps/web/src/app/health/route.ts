import { NextResponse } from "next/server";

export const dynamic = "force-dynamic";

export function GET() {
  return NextResponse.json(
    { status: "UP", service: "flowforge-web" },
    { headers: { "Cache-Control": "no-store" } },
  );
}
