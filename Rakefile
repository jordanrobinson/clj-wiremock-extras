# frozen_string_literal: true

require 'rake_leiningen'
require 'yaml'
require 'uri'

require_relative 'lib/leiningen_task_set'

task default: %i[library:check library:test:unit]

RakeLeiningen.define_installation_tasks(
  version: '2.10.0'
)

namespace :library do
  define_check_tasks(fix: true)

  namespace :test do
    RakeLeiningen.define_test_task(
      name: :unit,
      type: 'unit',
      profile: 'test')
  end

  namespace :publish do
    RakeLeiningen.define_release_task(
      name: :release,
      profile: 'release')  do |t|
          t.environment = {
              'VERSION' => ENV['VERSION'],
              'CLOJARS_DEPLOY_USERNAME' => ENV['CLOJARS_DEPLOY_USERNAME'],
              'CLOJARS_DEPLOY_TOKEN' => ENV['CLOJARS_DEPLOY_TOKEN']
          }
          end
  end

  desc 'Lint all src files'
  task :lint do
    puts "Running clj-kondo from ./scripts/lint for " + RUBY_PLATFORM
    platform_prefix = /darwin/ =~ RUBY_PLATFORM ? "mac" : "linux"

    sh("./scripts/lint/clj-kondo-2021-06-18-#{platform_prefix}",
       "--lint", "src/")

    puts "Finished running clj-kondo"
  end

  desc 'Reformat all src files'
  task :format do
    puts "Running cljstyle from ./scripts/lint for " + RUBY_PLATFORM
    platform_prefix = /darwin/ =~ RUBY_PLATFORM ? "mac" : "linux"

    sh("./scripts/lint/cljstyle-0-15-0-#{platform_prefix}", "fix")

    puts "Finished running cljstyle"
  end

  task :optimise do
    puts 'skipping optimise...'
  end
  task :idiomise do
    puts 'skipping idiomise...'
  end
end
