package com.detekt.lint

import com.detekt.lint.rule.ForbiddenBitmapInParcelableClassRule
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class CustomRuleSetProvider : RuleSetProvider {

    override val ruleSetId = "com-detekt-lint-custom-rule-set"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            id = ruleSetId,
            rules = listOf(
                ForbiddenBitmapInParcelableClassRule(config),
            ),
        )
    }
}
