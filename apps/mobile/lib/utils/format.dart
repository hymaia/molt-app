import 'package:flutter/widgets.dart';
import 'package:intl/intl.dart';

String formatEuros(BuildContext context, int cents) {
  final locale = Localizations.localeOf(context).toString();
  final euros = cents / 100;
  final fmt = euros == euros.roundToDouble()
      ? NumberFormat.decimalPattern(locale)
      : NumberFormat.decimalPatternDigits(locale: locale, decimalDigits: 2);
  return fmt.format(euros);
}

String formatDate(BuildContext context, DateTime date) {
  final locale = Localizations.localeOf(context).toString();
  return DateFormat.yMMMd(locale).format(date);
}
