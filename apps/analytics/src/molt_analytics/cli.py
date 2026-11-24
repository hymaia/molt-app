import argparse
from pathlib import Path

from . import config, export, report


def main(argv=None):
    parser = argparse.ArgumentParser(prog="molt-analytics", description="Molt marketplace analytics")
    sub = parser.add_subparsers(dest="command", required=True)

    r = sub.add_parser("report", help="print the marketplace report and write it as Markdown")
    r.add_argument("--source", choices=["csv", "api"], default="csv")
    r.add_argument("--output", default="reports/molt-report.md")
    r.add_argument("--lang", choices=["en", "fr", "it"], default=config.DEFAULT_LANG)
    r.add_argument("--client-kind", choices=["HUMAN", "AGENT", "HYBRID"])

    e = sub.add_parser("export", help="snapshot the API into CSV files")
    e.add_argument("--api-url", default=export.BASE)
    e.add_argument("--out-dir", default=str(config.DATA_DIR))

    args = parser.parse_args(argv)
    if args.command == "report":
        report.build(source=args.source, lang=args.lang, output=args.output, client_kind=args.client_kind)
    elif args.command == "export":
        for path in export.export_csv(Path(args.out_dir), args.api_url):
            print(f"wrote {path}")


if __name__ == "__main__":
    main()
