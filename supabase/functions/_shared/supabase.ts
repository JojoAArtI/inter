import { createClient, type SupabaseClient } from "npm:@supabase/supabase-js@2";
import { getEnv, requireEnv } from "./env.ts";

let adminClient: SupabaseClient | null = null;

export function getSupabaseAdminClient(): SupabaseClient {
  if (adminClient) {
    return adminClient;
  }

  const supabaseKey = getEnv("SUPABASE_SERVICE_ROLE_KEY") ??
    getEnv("SUPABASE_SECRET_KEY") ??
    requireEnv("SUPABASE_SERVICE_ROLE_KEY");

  adminClient = createClient(
    requireEnv("SUPABASE_URL"),
    supabaseKey,
    {
      auth: {
        autoRefreshToken: false,
        persistSession: false,
        detectSessionInUrl: false,
      },
    },
  );

  return adminClient;
}
